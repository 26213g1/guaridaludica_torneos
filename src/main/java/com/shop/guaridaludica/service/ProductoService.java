package com.shop.guaridaludica.service;

import com.shop.guaridaludica.model.Categoria;
import com.shop.guaridaludica.model.Imagen;
import com.shop.guaridaludica.model.Producto;
import com.shop.guaridaludica.repository.ProductoRepository;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;
    
    @Autowired
    private UploadFileService uploadFileService;

    public Page<Producto> getProductosPage(String query, Boolean sinCategoria, Boolean sinStock, BigDecimal minPrecio,
        BigDecimal maxPrecio, Pageable pageable, Categoria categoriaObj) {
        // 2. Llamada directa a nuestra @Query inteligente en el repositorio
        Page<Producto> productosPage = productoRepository.filtrarProductos(
                (query != null && !query.trim().isEmpty()) ? query.trim() : null,
                sinCategoria,
                sinStock,
                categoriaObj,
                minPrecio,
                maxPrecio,
                pageable
        );
        return productosPage;
    }

    public void altaProducto(Producto producto, MultipartFile[] files, Long imagenPrincipalId,
            Integer indicePrincipalNuevo, List<Long> idsImagenesAEliminar) {
        try {
            // 1. Si estamos editando, recuperamos el producto de la BBDD
            Producto productoPersistido = null;
            if (producto.getId() != null) {
                productoPersistido = productoRepository.findById(producto.getId()).orElse(null);
                if (productoPersistido != null) {
                    producto.setImagenes(productoPersistido.getImagenes());
                }
            }

            if (producto.getImagenes() == null) {
                producto.setImagenes(new ArrayList<>());
            }

            // 2. ELIMINAR IMÁGENES SELECCIONADAS
            if (idsImagenesAEliminar != null && !idsImagenesAEliminar.isEmpty()) {
                // Removemos de la colección las imágenes cuyo ID esté en la lista a eliminar
                producto.getImagenes().removeIf(img -> {
                    if (img.getId() != null && idsImagenesAEliminar.contains(img.getId())) {
                        // Opcional: Aquí podrías llamar a uploadFileService.deleteImage(img.getUrl()) si borras el fichero físico
                        return true; // Esto la borra de la lista (y gracias a orphanRemoval se borra de la BBDD)
                    }
                    return false;
                });
            }

            // 3. Procesamos y añadimos los nuevos archivos subidos (usando tu método helper!)
            for (int i = 0; i < files.length; i++) {
                MultipartFile file = files[i];
                if (file != null && !file.isEmpty()) {
                    String urlImagen = uploadFileService.saveImage(file);
                    if (urlImagen != null) {
                        Imagen imagen = new Imagen();
                        imagen.setUrl(urlImagen);
                        
                        if (indicePrincipalNuevo != null && indicePrincipalNuevo == i) {
                            imagen.setEsPrincipal(true);
                        } else {
                            imagen.setEsPrincipal(false);
                        }
                        
                        producto.agregarImagen(imagen);
                    }
                }
            }

            // 4. Gestionamos la imagen principal existente (si se marcó un radio de las antiguas)
            if (imagenPrincipalId != null) {
                for (Imagen img : producto.getImagenes()) {
                    if (img.getId() != null && img.getId().equals(imagenPrincipalId)) {
                        img.setEsPrincipal(true);
                    } else {
                        img.setEsPrincipal(false);
                    }
                }
            } else if (indicePrincipalNuevo != null) {
                for (Imagen img : producto.getImagenes()) {
                    if (img.getId() == null) { // Si es una de las nuevas marcadas
                        img.setEsPrincipal(true);
                    } else {
                        img.setEsPrincipal(false);
                    }
                }
            }

            // 5. Red de seguridad: si ninguna es principal, la primera lo es
            if (!producto.getImagenes().isEmpty()) {
                boolean algunaEsPrincipal = producto.getImagenes().stream().anyMatch(Imagen::isEsPrincipal);
                if (!algunaEsPrincipal) {
                    producto.getImagenes().get(0).setEsPrincipal(true);
                }
            }

            // 6. Guardamos el producto (la cascada y orphanRemoval hacen la magia)
            productoRepository.save(producto);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

  
}
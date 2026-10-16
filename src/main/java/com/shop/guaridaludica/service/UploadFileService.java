package com.shop.guaridaludica.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class UploadFileService {

    /* // Carpeta donde se guardarán las imágenes de los productos
    private final String folder = "src/main/resources/static/images/products/";

    public String saveImage(MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            byte[] bytes = file.getBytes();
            // Generamos un nombre único para evitar que se sobrescriban archivos con el mismo nombre
            String uniqueFilename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path path = Paths.get(folder + uniqueFilename);
            Files.write(path, bytes);
            
            // Retornamos la ruta relativa que se guardará en la BBDD
            return "/images/products/" + uniqueFilename;
        }
        return null;
    } */

    @Autowired
    private Cloudinary cloudinary;

    public String saveImage(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return null;
        }

        // Subimos la imagen a Cloudinary dentro de una carpeta llamada "guarida_productos"
        Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), 
                ObjectUtils.asMap("folder", "guarida_productos"));

        // Retornamos la URL segura (https) de la imagen subida
        return uploadResult.get("secure_url").toString();
    }

    public void deleteImage(String imageUrl) {
        // Opcional: Si en algún momento quieres borrar la imagen de Cloudinary al eliminar un producto
        try {
            if (imageUrl != null && imageUrl.contains("guarida_productos")) {
                // Extraer el public_id de la URL de Cloudinary si lo necesitas
                // Por ahora puedes dejarlo vacío o implementarlo si borras productos con frecuencia
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
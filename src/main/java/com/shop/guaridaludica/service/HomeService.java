package com.shop.guaridaludica.service;

import com.shop.guaridaludica.model.Categoria;
import com.shop.guaridaludica.model.Producto;
import com.shop.guaridaludica.repository.CategoriaRepository;
import com.shop.guaridaludica.repository.ProductoRepository;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class HomeService {

    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private CategoriaRepository categoriaRepository;

    // Obtener lista de productos paginada, con o sin filtro de búsqueda
    public Page<Producto> obtenerProductosPaginados(Pageable pageable) {
        return productoRepository.findAllActive(pageable);
    }

    public Page<Producto> obtenerProductosPaginadosPorCategoria(Long id, Pageable pageable) {
        // 1. Buscamos la categoría para saber su nombre (opcional, por si quieres
        // mostrarlo en la vista)
        Categoria categoria = categoriaRepository.findById(id).orElse(null);

        // 2. Buscamos los productos asociados a esa categoría
        // Nota: Asegúrate de tener un método en el repositorio que busque por
        // categoría,
        // algo como: List<Producto> findByCategoriaId(Long categoriaId);
        Page<Producto> productos = productoRepository.findByCategoriasContaining(categoria, pageable);

        return productos;
    }

    public Page<Producto> obtenerProductosPaginadosBuscador(String query, Pageable pageable) {
        return productoRepository.findByNombreContainingIgnoreCaseAndCategoriasIsNotEmptyList(query, pageable);
    }

    // Nuevo método para manejar los filtros avanzados de la tienda pública
    public Page<Producto> filtrarProductosPublicos(Long categoriaId, BigDecimal maxPrecio, Integer jugadores, String duracion, Pageable pageable) {
        Categoria categoria = null;
        if (categoriaId != null) {
            categoria = categoriaRepository.findById(categoriaId).orElse(null);
        }
        
        return productoRepository.filtrarProductosPublicos(categoria, maxPrecio, jugadores, duracion, pageable);
    }
}
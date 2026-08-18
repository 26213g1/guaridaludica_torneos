package com.shop.guaridaludica.repository;

import com.shop.guaridaludica.model.Categoria;
import com.shop.guaridaludica.model.Producto;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    
    // Método para la tienda pública: solo productos que NO tengan el Set de categorías vacío
    @Query("SELECT p FROM Producto p WHERE p.categorias IS NOT EMPTY")
    Page<Producto> findAllActive(Pageable pageable);

    // Búsqueda por nombre (ignorando mayúsculas/minúsculas)
    Page<Producto> findByNombreContainingIgnoreCase(String nombre, Pageable pageable);

    // Productos huérfanos (donde la categoría sea nula)
    Page<Producto> findByCategoriasIsNull(Pageable pageable);

    // Búsqueda combinada opcional para sin categoría + nombre
    Page<Producto> findByCategoriasIsNullAndNombreContainingIgnoreCase(String nombre, Pageable pageable);

    // Buscar productos que contengan una categoría específica en su Set<Categoria> Paginado
    Page<Producto> findByCategoriasContaining(Categoria categori, Pageable pageablea);

    // Buscar productos que contengan una categoría específica en su Set<Categoria>
    List<Producto> findByCategoriasContaining(Categoria categoria);

    @Query("SELECT p FROM Producto p WHERE LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')) AND p.categorias IS NOT EMPTY")
    Page<Producto> findByNombreContainingIgnoreCaseAndCategoriasIsNotEmptyList(@Param("nombre") String nombre, Pageable pageable);

    // Productos entre un precio mínimo y máximo (con BigDecimal)
    Page<Producto> findByPrecioBetween(BigDecimal minPrecio, BigDecimal maxPrecio, Pageable pageable);

    // Consulta unificada avanzada para la administración (Soporta nombre, sin categoría, categoría específica y rango de precios)
    @Query("SELECT p FROM Producto p WHERE "
            + "(:query IS NULL OR LOWER(p.nombre) LIKE LOWER(CONCAT('%', :query, '%'))) AND "
            + "(:sinCategoria IS NULL OR p.categorias IS EMPTY) AND "
            + "(:categoria IS NULL OR :categoria MEMBER OF p.categorias) AND "
            + "(:minPrecio IS NULL OR p.precio >= :minPrecio) AND " 
            + "(:maxPrecio IS NULL OR p.precio <= :maxPrecio)")
    Page<Producto> filtrarProductos(
            @Param("query") String query, 
            @Param("sinCategoria") Boolean sinCategoria,
            @Param("categoria") Categoria categoria, 
            @Param("minPrecio") BigDecimal minPrecio,
            @Param("maxPrecio") BigDecimal maxPrecio, 
            Pageable pageable
    );
}
package com.shop.guaridaludica.controller;

import com.shop.guaridaludica.model.Categoria;
import com.shop.guaridaludica.model.Producto;
import com.shop.guaridaludica.repository.CategoriaRepository;
import com.shop.guaridaludica.repository.ProductoRepository;
import com.shop.guaridaludica.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Controller
@RequestMapping("/admin/productos")
public class ProductoAdminController {
    @Autowired
    private ProductoService productoService;
    @Autowired
    public ProductoRepository productoRepository;

    @Autowired
    public CategoriaRepository categoriaRepository;

    // Inyectamos un tamaño diferente específico para administración
    @Value("${shop.pagination.admin-size}")
    private int adminSize;

    @GetMapping("")
    public String listarProductos(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) Boolean sinCategoria,
            @RequestParam(required = false) Long categoriaId,
            @RequestParam(required = false) Boolean sinStock,
            @RequestParam(required = false) BigDecimal minPrecio,
            @RequestParam(required = false) BigDecimal maxPrecio,
            @RequestParam(defaultValue = "0") int page, 
            Model model) {

        Pageable pageable = PageRequest.of(page, adminSize);
        
        // 1. Buscamos el objeto Categoria si el admin ha seleccionado un ID en el desplegable
        Categoria categoriaObj = null;
        if (categoriaId != null) {
            categoriaObj = categoriaRepository.findById(categoriaId).orElse(null);
        }

        Page<Producto> productosPage = productoService.getProductosPage(query, sinCategoria, sinStock, minPrecio, maxPrecio, pageable,
                categoriaObj);
        
        // 3. Atributos para la paginación y la tabla
        model.addAttribute("productos", productosPage.getContent());
        model.addAttribute("currentPage", productosPage.getNumber());
        model.addAttribute("totalPages", productosPage.getTotalPages());
        model.addAttribute("hasPrevious", productosPage.hasPrevious());
        model.addAttribute("hasNext", productosPage.hasNext());
        
        // 4. Datos para poblar los filtros en la vista
        model.addAttribute("todasCategorias", categoriaRepository.findAll());
        model.addAttribute("query", query);
        model.addAttribute("sinCategoria", sinCategoria);
        model.addAttribute("categoriaId", categoriaId);
        model.addAttribute("sinStock", sinStock);
        model.addAttribute("minPrecio", minPrecio);
        model.addAttribute("maxPrecio", maxPrecio);

        return "admin/productos/listar";
    }


    // Mostrar formulario para CREAR nuevo producto
    @GetMapping("/nuevo")
    public String crearProductoForm(Model model) {
        model.addAttribute("producto", new Producto());
        model.addAttribute("todasCategorias", categoriaRepository.findAll());
        return "admin/productos/form-producto";
    }

    // Mostrar formulario para EDITAR producto existente
    @GetMapping("/editar/{id}")
    public String editarProductoForm(@PathVariable Long id, Model model) {
        Producto producto = productoRepository.findById(id).orElse(null);
        if (producto == null) {
            return "redirect:/admin/productos";
        }
        model.addAttribute("producto", producto);
        model.addAttribute("todasCategorias", categoriaRepository.findAll());
        return "admin/productos/form-producto";
    }


    // Guardar (Sirve tanto para INSERTAR como para ACTUALIZAR)
    @PostMapping("/guardar")
    public String guardarProducto(@ModelAttribute Producto producto,
                                  @RequestParam("files") MultipartFile[] files,
                                  @RequestParam(value = "imagenPrincipalId", required = false) Long imagenPrincipalId,
                                  @RequestParam(value = "indicePrincipalNuevo", required = false) Integer indicePrincipalNuevo,
                                  @RequestParam(value = "idsImagenesAEliminar", required = false) List<Long> idsImagenesAEliminar) {
        productoService.altaProducto(producto, files, imagenPrincipalId, indicePrincipalNuevo, idsImagenesAEliminar);

        return "redirect:/admin/productos";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarProducto(@PathVariable Long id) {
        productoRepository.deleteById(id);
        return "redirect:/admin/productos";
    }

}
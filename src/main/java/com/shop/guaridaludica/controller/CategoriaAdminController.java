package com.shop.guaridaludica.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.shop.guaridaludica.model.Categoria;
import com.shop.guaridaludica.model.Producto;
import com.shop.guaridaludica.repository.CategoriaRepository;
import com.shop.guaridaludica.repository.ProductoRepository;

import org.springframework.ui.Model;

@Controller
@RequestMapping("/admin/categorias")
public class CategoriaAdminController {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    // 1. Listar categorías
    @GetMapping
    public String listarCategorias(Model model) {
        categoriaRepository.findAll();
        model.addAttribute("categorias", categoriaRepository.findAll());
        return "admin/categorias/listar";
    }

    // 2. Formulario para nueva categoría
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevaCategoria(Model model) {
        model.addAttribute("categoria", new Categoria());
        return "admin/categorias/form-categoria";
    }

    // 3. Formulario para editar categoría existente
    @GetMapping("/editar/{id}")
    public String editarCategoria(@PathVariable Long id, Model model) {
        Categoria categoria = categoriaRepository.findById(id).orElseThrow();

        // Obtenemos los productos asociados
        List<Producto> productosAsociados = productoRepository.findByCategoriasContaining(categoria);

        model.addAttribute("categoria", categoria);
        model.addAttribute("productosAsociados", productosAsociados);

        return "admin/categorias/form-categoria"; // o como se llame tu vista
    }

    // 4. Guardar (Crear o Actualizar)
    @PostMapping("/guardar")
    public String guardarCategoria(@ModelAttribute Categoria categoria) {
        categoriaRepository.save(categoria);
        return "redirect:/admin/categorias";
    }

    // 5. Eliminar categoría
    @GetMapping("/eliminar/{id}")
    public String eliminarCategoria(@PathVariable Long id) {
        // 1. Buscamos la categoría
        Categoria categoria = categoriaRepository.findById(id).orElseThrow();

        // 2. Buscamos todos los productos que tienen asignada esta categoría
        List<Producto> productos = productoRepository.findByCategoriasContaining(categoria);

        // 3. Desvinculamos la categoría de cada producto de forma segura
        for (Producto producto : productos) {
            producto.getCategorias().remove(categoria);
            productoRepository.save(producto); // Actualiza la tabla intermedia eliminando la relación
        }

        // 4. Una vez limpia la relación, ya podemos borrar la categoría sin que salte
        // el error de clave foránea
        categoriaRepository.delete(categoria);

        return "redirect:/admin/categorias";
    }

    @GetMapping("/quitar-asociacion")
    public String quitarAsociacion(@RequestParam Long categoriaId, @RequestParam Long productoId) {
        Producto producto = productoRepository.findById(productoId).orElseThrow();
        Categoria categoria = categoriaRepository.findById(categoriaId).orElseThrow();

        // Quitamos la categoría del Set del producto
        producto.getCategorias().remove(categoria);
        productoRepository.save(producto);

        // Redirigimos de vuelta al formulario de edición de la categoría
        return "redirect:/admin/categorias/editar/" + categoriaId;
    }
}

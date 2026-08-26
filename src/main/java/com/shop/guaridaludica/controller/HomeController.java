package com.shop.guaridaludica.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.shop.guaridaludica.config.ConfiguracionApp;
import com.shop.guaridaludica.model.Producto;
import com.shop.guaridaludica.service.HomeService;
import com.shop.guaridaludica.repository.CategoriaRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Controller
public class HomeController {
    @Autowired
    private CategoriaRepository categoriaRepository;
    @Autowired
    private HomeService homeService;
    @Autowired
    private ConfiguracionApp configuracionApp;

    // Inyectamos el valor desde el YAML
    @Value("${shop.pagination.public-size}")
    private int publicSize;

    @GetMapping("/")
    public String inicio(@RequestParam(defaultValue = "0") int page, Model model) {
        // Obtenemos todos los juegos de la base de datos
        // Mostramos publicSize productos por página
        Pageable pageable = PageRequest.of(page, publicSize);
        Page<Producto> productosPage = homeService.obtenerProductosPaginados(pageable);
        
        envioHtml(page, model, productosPage);

        model.addAttribute("mensaje", "¡Bienvenido a tu tienda de juegos de mesa modernos!");
        return "index"; // Esto buscará una vista llamada index.html
    }

    @GetMapping("/categoria/{id}")
    public String listarPorCategoria(@RequestParam(defaultValue = "0") int page, @PathVariable Long id, Model model) {
        Pageable pageable = PageRequest.of(page, publicSize);
        Page<Producto> productosPage = homeService.obtenerProductosPaginadosPorCategoria(id, pageable);
        
        envioHtml(page, model, productosPage);

        // ¡IMPORTANTE! Pasamos también las categorías para que el menú lateral izquierdo no falle
        //model.addAttribute("categorias", categoriaRepository.findAll());
        return "index"; // O tu vista de catálogo
    }

    @GetMapping("/buscar")
    public String buscarProductos(@RequestParam(defaultValue = "0") int page, @RequestParam("query") String query, Model model) {
        Pageable pageable = PageRequest.of(page, publicSize);
        // 1. Buscamos los productos
        Page<Producto> productosEncontrados = homeService.obtenerProductosPaginadosBuscador(query, pageable);

        envioHtml(page, model, productosEncontrados);

        model.addAttribute("busqueda", query);

        // ¡IMPORTANTE! Pasamos también las categorías para que el menú lateral izquierdo no falle
        //model.addAttribute("categorias", categoriaRepository.findAll());
        return "index"; // o la vista que utilices para el catálogo principal
    }

    private void envioHtml(int page, Model model, Page<Producto> productosPage) {
        // Los enviamos a la vista HTML
        model.addAttribute("productos", productosPage.getContent()); // La lista de productos de esta página
        // AQUÍ ESTÁ LA CLAVE: Añadimos los días configurados en el yaml al modelo
        model.addAttribute("diasNuevo", configuracionApp.getDiasProductoNuevo());
        // Y si usas datos generales de la app en la vista, también puedes pasar la config entera
        model.addAttribute("appConfig", configuracionApp);
        
        model.addAttribute("currentPage", page);                     // Página actual
        model.addAttribute("totalPages", productosPage.getTotalPages()); // Total de páginas
        model.addAttribute("hasPrevious", productosPage.hasPrevious());
        model.addAttribute("hasNext", productosPage.hasNext());

        // ¡IMPORTANTE! Pasamos también las categorías para que el menú lateral izquierdo no falle
        model.addAttribute("categorias", categoriaRepository.findAll());
    }

}
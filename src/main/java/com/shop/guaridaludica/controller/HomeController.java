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

import java.math.BigDecimal;
import java.util.List;

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

    @Value("${tienda.dias-producto-nuevo}")
    private int diasNuevo;

    // Modificamos el inicio para que acepte filtros opcionales por GET
    @GetMapping({"/", "/tienda"})
    public String inicio(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) Long categoriaId,
            @RequestParam(required = false) BigDecimal maxPrecio,
            @RequestParam(required = false) Integer jugadores,
            @RequestParam(required = false) String rangoDuracion,
            Model model) {
        
        Pageable pageable = PageRequest.of(page, publicSize);
        
        // Llamamos al servicio de filtrado público que creamos antes
        Page<Producto> productosPage = homeService.filtrarProductosPublicos(categoriaId, maxPrecio, jugadores, rangoDuracion, pageable);
        
        envioHtml(page, model, productosPage);

        if(configuracionApp.isMostrarNovedadesHome()){
            List<Producto> productosNuevos = homeService.getProductosNuevos(diasNuevo);
            model.addAttribute("productosNuevos", productosNuevos);
        }
        model.addAttribute("mostrarNovedadesHome", configuracionApp.isMostrarNovedadesHome());


        if(configuracionApp.isMostrarDestacadosHome()){
            List<Producto> productosDestacados = homeService.getProductosDestacados();
            model.addAttribute("productosDestacados", productosDestacados);
        }
        model.addAttribute("mostrarDestacados", configuracionApp.isMostrarDestacadosHome());


        // Pasamos los valores actuales al modelo para que los inputs recuerden la selección
        model.addAttribute("categoriaSeleccionada", categoriaId);
        model.addAttribute("maxPrecio", maxPrecio);
        model.addAttribute("jugadores", jugadores);
        model.addAttribute("rangoDuracion", rangoDuracion);
        
        model.addAttribute("mensaje", "¡Bienvenido a tu tienda de juegos de mesa modernos!");
        return "index";
    }

    

    @GetMapping("/categoria/{id}")
    public String listarPorCategoria(@RequestParam(defaultValue = "0") int page, @PathVariable Long id, Model model) {
        Pageable pageable = PageRequest.of(page, publicSize);
        Page<Producto> productosPage = homeService.obtenerProductosPaginadosPorCategoria(id, pageable);
        
        envioHtml(page, model, productosPage);
        model.addAttribute("categoriaSeleccionada", id); // Para marcar la categoría activa si procede
        return "index";
    }

    @GetMapping("/buscar")
    public String buscarProductos(@RequestParam(defaultValue = "0") int page, @RequestParam("query") String query, Model model) {
        Pageable pageable = PageRequest.of(page, publicSize);
        Page<Producto> productosEncontrados = homeService.obtenerProductosPaginadosBuscador(query, pageable);

        envioHtml(page, model, productosEncontrados);
        model.addAttribute("busqueda", query);
        return "index";
    }

    private void envioHtml(int page, Model model, Page<Producto> productosPage) {
        model.addAttribute("productos", productosPage.getContent());
        model.addAttribute("diasNuevo", configuracionApp.getDiasProductoNuevo());
        model.addAttribute("appConfig", configuracionApp);
        
        model.addAttribute("currentPage", page);                    
        model.addAttribute("totalPages", productosPage.getTotalPages());
        model.addAttribute("hasPrevious", productosPage.hasPrevious());
        model.addAttribute("hasNext", productosPage.hasNext());

        model.addAttribute("categorias", categoriaRepository.findAll());
    }
}
package com.shop.guaridaludica.controller;

import com.shop.guaridaludica.model.Usuario;
import com.shop.guaridaludica.service.UsuarioService;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/usuarios")
public class UsuarioAdminController {

    @Autowired
    private UsuarioService usuarioService;

    // Inyectamos un tamaño diferente específico para administración
    @Value("${shop.pagination.admin-size}")
    private int adminSize;


    @GetMapping("")
    public String listarUsuarios(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int page,
            Model model) {

        Pageable pageable = PageRequest.of(page, adminSize);
        Page<Usuario> usuariosPage = usuarioService.obtenerUsuariosPaginados(keyword, pageable);

        model.addAttribute("usuarios", usuariosPage);
        model.addAttribute("keyword", keyword);
                
        // Pasamos el servicio al modelo para poder llamarlo desde la vista de Thymeleaf
        model.addAttribute("usuarioService", usuarioService);

        model.addAttribute("currentPage", usuariosPage.getNumber());
        model.addAttribute("totalPages", usuariosPage.getTotalPages());
        model.addAttribute("hasPrevious", usuariosPage.hasPrevious());
        model.addAttribute("hasNext", usuariosPage.hasNext());


        return "admin/usuarios/listar";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable Long id, Model model) {
        Optional<Usuario> usuarioOpt = usuarioService.buscarPorId(id);
        
        if (usuarioOpt.isEmpty()) {
            return "redirect:/admin/usuarios?error=noencontrado";
        }

        model.addAttribute("usuario", usuarioOpt.get());
        // Pasamos una lista de roles disponibles en tu sistema (ej: ROLE_USER, ROLE_ADMIN)
        model.addAttribute("rolesDisponibles", List.of("ROLE_USER", "ROLE_ADMIN"));

        return "admin/usuarios/editar"; // Vista del formulario de edición
    }

    @PostMapping("/editar/{id}")
    public String guardarEdicion(@PathVariable Long id, @RequestParam(required = false)  Set<String> roles) {
        // Si desmarcan todo y llega null, aseguramos un rol mínimo por seguridad
        if (roles == null || roles.isEmpty()) {
            //roles = Set.of("ROLE_USER");
            // Usamos HashSet para que sea una colección mutable que Hibernate pueda manejar
            roles = new java.util.HashSet<>(java.util.Set.of("ROLE_USER"));
        }
        usuarioService.actualizarRoles(id, roles);
        return "redirect:/admin/usuarios?exito=editado";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        return "redirect:/admin/usuarios?exito=eliminado";
    }

}
package com.shop.guaridaludica.controller;

import com.shop.guaridaludica.model.Direccion;
import com.shop.guaridaludica.model.Pedido;
import com.shop.guaridaludica.model.Usuario;
import com.shop.guaridaludica.repository.DireccionRepository;
import com.shop.guaridaludica.repository.PedidoRepository;
import com.shop.guaridaludica.repository.UsuarioRepository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/perfil")
public class PerfilController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private DireccionRepository direccionRepository; // Asegúrate de tener este repositorio creado

    @Autowired
    private PedidoRepository pedidoRepository;

    // 1. Ver el panel de la cuenta y su libreta de direcciones
    @GetMapping
    public String verPerfil(Model model, Authentication authentication) {
        String alias = authentication.getName(); // El email del usuario logueado
        Usuario usuario = usuarioRepository.findByAlias(alias)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 1. Buscamos todos los pedidos del usuario
        List<Pedido> misPedidos = pedidoRepository.findByUsuarioOrderByFechaPedidoDesc(usuario);

        // 2. Filtramos la lista para OMITIR los que estén CANCELADOS
        List<Pedido> pedidosActivos = misPedidos.stream().filter(p -> p.getEstado() != Pedido.EstadoPedido.CANCELADO)
                .collect(Collectors.toList()); // Si usas Java 16+, si no usa .collect(Collectors.toList())

        model.addAttribute("usuario", usuario);
        model.addAttribute("nuevaDireccion", new Direccion()); // Objeto vacio para el formulario de añadir
        model.addAttribute("pedidos", pedidosActivos);
        return "perfil/cuenta"; // Ruta de la vista HTML
    }

    // 2. Guardar una nueva dirección en la agenda del usuario
    @PostMapping("/direcciones/guardar")
    public String guardarDireccion(@ModelAttribute Direccion direccion, Authentication authentication) {
        String alias = authentication.getName();
        Usuario usuario = usuarioRepository.findByAlias(alias)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // LÓGICA DE DIRECCIÓN PRINCIPAL ÚNICA:
        // Si la dirección que están guardando viene marcada como principal (true):
        if (direccion.isEsPrincipal()) { // O si es getPrincipal(), dependiendo de tu getter booleano
            // Recorremos las direcciones actuales del usuario y las ponemos a false
            for (Direccion d : usuario.getDirecciones()) {
                d.setEsPrincipal(false);
            }
        }
        // CASO EXTRA: Si es la primera dirección que añade el usuario, por defecto la
        // hacemos principal
        else if (usuario.getDirecciones().isEmpty()) {
            direccion.setEsPrincipal(true);
        }

        // Usamos tu método helper o asociamos la dirección al usuario
        usuario.addDireccion(direccion);
        usuarioRepository.save(usuario); // Gracias al CascadeType.ALL, guardará la dirección automáticamente

        return "redirect:/perfil";
    }

    // 3. Eliminar una dirección de la agenda
    @PostMapping("/direcciones/eliminar")
    public String eliminarDireccion(@RequestParam Long id, Authentication authentication) {
        // Validamos opcionalmente que la dirección pertenezca al usuario logueado por
        // seguridad
        direccionRepository.findById(id).ifPresent(direccion -> {
            if (direccion.getUsuario().getAlias().equals(authentication.getName())) {
                direccionRepository.delete(direccion);
            }
        });

        return "redirect:/perfil";
    }

    // 4. Actualizar datos personales del usuario
    @PostMapping("/actualizar-datos")
    public String actualizarDatos(@ModelAttribute Usuario usuarioForm, Authentication authentication) {
        String aliasActual = authentication.getName();
        Usuario usuario = usuarioRepository.findByAlias(aliasActual)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Actualizamos los campos permitidos
        usuario.setNombre(usuarioForm.getNombre());
        usuario.setTelefono(usuarioForm.getTelefono());

        usuarioRepository.save(usuario);

        // Nota: Si cambias el alias, es posible que tengas que actualizar el contexto
        // de seguridad
        // para que authentication.getName() no falle en la siguiente recarga,
        // o puedes evitar que cambien el alias y permitir solo nombre, teléfono y
        // email.

        return "redirect:/perfil";
    }

    // 5. Mostrar formulario para editar una dirección existente
    @GetMapping("/direcciones/editar/{id}")
    public String editarDireccionForm(@PathVariable Long id, Model model, Authentication authentication) {
        String alias = authentication.getName();
        Usuario usuario = usuarioRepository.findByAlias(alias)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Buscamos la dirección y verificamos por seguridad que pertenece al usuario
        // logueado
        Direccion direccion = direccionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dirección no encontrada"));

        if (!direccion.getUsuario().getId().equals(usuario.getId())) {
            return "redirect:/perfil"; // Evita que un usuario edite direcciones ajenas
        }

        model.addAttribute("usuario", usuario);
        model.addAttribute("direccion", direccion);
        return "perfil/editar-direccion"; // Vista específica para editar o un modal
    }

    // 6. Procesar la actualización de la dirección vía Modal
    @PostMapping("/direcciones/actualizar")
    public String actualizarDireccion(@RequestParam Long id, @ModelAttribute Direccion direccionForm,
            Authentication authentication) {
        String alias = authentication.getName();
        Usuario usuario = usuarioRepository.findByAlias(alias)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Direccion direccionExistente = direccionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dirección no encontrada"));

        // Seguridad: comprobar propiedad
        if (!direccionExistente.getUsuario().getId().equals(usuario.getId())) {
            return "redirect:/perfil";
        }

        // Si se marca como principal, desmarcamos las demás del usuario
        if (direccionForm.isEsPrincipal()) {
            for (Direccion d : usuario.getDirecciones()) {
                d.setEsPrincipal(false);
            }
        }

        // Actualizamos los campos necesarios de la dirección
        direccionExistente.setAlias(direccionForm.getAlias()); // ¡Añadido por si editas el alias!
        direccionExistente.setCalle(direccionForm.getCalle());
        direccionExistente.setNumero(direccionForm.getNumero()); // ¡Añadido el número!
        direccionExistente.setCiudad(direccionForm.getCiudad());
        direccionExistente.setCodigoPostal(direccionForm.getCodigoPostal());
        direccionExistente.setPais(direccionForm.getPais()); // ¡Añadido el país!
        direccionExistente.setEsPrincipal(direccionForm.isEsPrincipal());

        direccionRepository.save(direccionExistente);

        return "redirect:/perfil";
    }
}
package com.shop.guaridaludica.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shop.guaridaludica.carrito.Carrito;
import com.shop.guaridaludica.carrito.CarritoItem;
import com.shop.guaridaludica.model.DetallePedido;
import com.shop.guaridaludica.model.Direccion;
import com.shop.guaridaludica.model.Pedido;
import com.shop.guaridaludica.model.Producto;
import com.shop.guaridaludica.model.Usuario;
import com.shop.guaridaludica.repository.DetallePedidoRepository;
import com.shop.guaridaludica.repository.DireccionRepository;
import com.shop.guaridaludica.repository.PedidoRepository;
import com.shop.guaridaludica.repository.ProductoRepository;
import com.shop.guaridaludica.repository.UsuarioRepository;
import com.shop.guaridaludica.service.PedidoService;

import org.springframework.security.core.Authentication;

@Controller
@RequestMapping("/pedido")
public class PedidoController {

    @Autowired
    private Carrito carrito;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private DetallePedidoRepository detallePedidoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private DireccionRepository direccionRepository;

    @Autowired
    private PedidoService pedidoService;

    @GetMapping("/checkout")
    public String mostrarCheckout(Model model, Authentication authentication) {
        if (carrito.getItems().isEmpty()) {
            return "redirect:/carrito";
        }

        String alias = authentication.getName();
        Usuario usuario = usuarioRepository.findByAlias(alias).orElseThrow(() -> new RuntimeException("Usuario no encontrado con alias: " + alias));
    
        model.addAttribute("direccionesGuardadas", direccionRepository.findByUsuarioId(usuario.getId()));
        model.addAttribute("items", carrito.getItems());
        model.addAttribute("total", carrito.getTotal());
        model.addAttribute("direccion", new Direccion()); // Para enlazar el formulario

        return "pedido/checkout";
    }

    @PostMapping("/procesar")
    public String procesarPedido(@ModelAttribute Direccion direccion, Authentication authentication, Model model) {
        if (carrito.getItems().isEmpty()) {
            return "redirect:/carrito";
        }

        // Obtener el alias del usuario logueado a través de Spring Security
        String aliasUsuario = authentication.getName();
        // Buscar el usuario en la base de datos
        Usuario usuario = usuarioRepository.findByAlias(aliasUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con alias: " + aliasUsuario));

        // Guardar dirección en el perfil del usuario si no existe (opcional pero
        // profesional)
        // Asumiendo que tienes un repositorio de Direcciones

        boolean existe = direccionRepository.existsByUsuarioAndCalleAndNumeroAndCiudadAndCodigoPostal(
            usuario, 
            direccion.getCalle(), 
            direccion.getNumero(), 
            direccion.getCiudad(), 
            direccion.getCodigoPostal()
        );

        if (!existe) {
            direccion.setUsuario(usuario);
            direccionRepository.save(direccion);
        } else {
            // Opcional: Si ya existe, puedes recuperar la existente de la BD para asociarla al pedido 
            // en lugar de crear una nueva instancia huérfana.
        }

        // Crear el Pedido y asociar los datos necesarios (según tus entidades JPA)
        Pedido pedido = new Pedido();
        pedido.setDireccionEnvio(direccion);
        pedido.setTotal(carrito.getTotal());
        pedido.setUsuario(usuario);
        pedido.setEstado(Pedido.EstadoPedido.PENDIENTE_PAGO);

        // Guardamos y forzamos el flush para que el ID se genere al instante
        pedido = pedidoRepository.saveAndFlush(pedido);

        // Guardar los detalles del pedido desde el carrito
        for (CarritoItem item : carrito.getItems()) {
            // Buscamos el producto real en la base de datos usando el ID del item del carrito
            Producto producto = productoRepository.findById(item.getProductoId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + item.getProductoId()));

            DetallePedido detalle = new DetallePedido();
            detalle.setPedido(pedido);
            detalle.setProducto(producto);
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioUnitario(item.getPrecio());
            detallePedidoRepository.save(detalle);
        }
        // Guardar en Base de Datos
        pedidoRepository.save(pedido);

        // Redirigir a la pasarela de pago simulada con el ID del pedido
        return "redirect:/pedido/pagar/" + pedido.getId();
    }

    @GetMapping("/pagar/{id}")
    public String mostrarPasarelaPago(@PathVariable Long id, Model model) {
        Pedido pedido = pedidoRepository.findById(id).orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        model.addAttribute("pedido", pedido);
        return "pedido/pasarela-pago"; // Vista de la pasarela simulada
    }

    @PostMapping("/confirmar-pago/{id}")
    public String confirmarPago(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Pedido pedido = pedidoRepository.findById(id).orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

            // 1. Descontar el stock de forma segura antes de darlo por pagado
            pedidoService.descontarStock(pedido);

            // 2. Actualizar estado a pagado
            pedido.setEstado(Pedido.EstadoPedido.PAGADO);
            pedidoRepository.save(pedido);

            // 3. Vaciar el carrito de la sesión
            carrito.vaciar();

            return "redirect:/pedido/exitoso?id=" + pedido.getId();
        } catch (RuntimeException e) {
            // Si salta un error de stock en el último segundo, cancelamos el pedido y avisamos
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/carrito";
        }
    }

    @PostMapping("/cancelar/{id}")
    public String cancelarPedido(@PathVariable Long id) {
        Pedido pedido = pedidoRepository.findById(id).orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        // Marcar como cancelado (o eliminar si prefieres no dejar rastro)
        pedido.setEstado(Pedido.EstadoPedido.CANCELADO);
        pedidoRepository.save(pedido);

        // El carrito se mantiene intacto, devolvemos al usuario al carrito
        return "redirect:/carrito";
    }

    @GetMapping("/exitoso")
    public String pedidoExitoso(Long id, Model model) {
        model.addAttribute("idPedido", id);
        return "pedido/pedido-exitoso";
    }

}

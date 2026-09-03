package com.shop.guaridaludica.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.shop.guaridaludica.model.DetallePedido;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String remiteEmail;

    @Value("${contacto.email}")
    private String soporteEmail;

    @Async
    public void enviarCorreoBienvenida(String destinatario, String nombreUsuario) {
        try {
            MimeMessage mensaje = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");

            helper.setFrom(soporteEmail);
            helper.setTo(destinatario);
            helper.setSubject("🎲 ¡Bienvenido a La Guarida Lúdica!");

            // Variables que pasamos a la plantilla HTML
            Context context = new Context();
            context.setVariable("nombre", nombreUsuario);

            // Procesa la plantilla ubicada en resources/templates/email/bienvenida.html
            String contenidoHtml = templateEngine.process("email/bienvenida", context);
            helper.setText(contenidoHtml, true);

            mailSender.send(mensaje);
        } catch (MessagingException e) {
            // Manejar la excepción o loguearla
            e.printStackTrace();
        }
    }

    @Async
    public void enviarCorreoConfirmacionPedido(String destinatario, Long pedidoId, List<DetallePedido> detalles, BigDecimal total) {
        try {
            MimeMessage mensaje = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");

            helper.setFrom(soporteEmail);
            helper.setTo(destinatario);
            helper.setSubject("📦 ¡Pedido confirmado #" + pedidoId + " - La Guarida Lúdica!");

            Context context = new Context();
            context.setVariable("pedidoId", pedidoId);
            context.setVariable("detalles", detalles);
            context.setVariable("total", total);

            String contenidoHtml = templateEngine.process("email/pedido-confirmado", context);
            helper.setText(contenidoHtml, true);

            mailSender.send(mensaje);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Async
    public void enviarCorreoEnvio(String destinatario, Long pedidoId, List<DetallePedido> detalles) {
        try {
            MimeMessage mensaje = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");

            helper.setFrom(soporteEmail);
            helper.setTo(destinatario);
            helper.setSubject("🚚 ¡Tus juegos de mesa de La Guarida Lúdica van de camino! (Pedido #" + pedidoId + ")");

            Context context = new Context();
            context.setVariable("pedidoId", pedidoId);
            context.setVariable("detalles", detalles);

            String contenidoHtml = templateEngine.process("email/pedido-enviado", context);
            helper.setText(contenidoHtml, true);

            mailSender.send(mensaje);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
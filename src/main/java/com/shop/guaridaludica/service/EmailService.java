package com.shop.guaridaludica.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

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
}
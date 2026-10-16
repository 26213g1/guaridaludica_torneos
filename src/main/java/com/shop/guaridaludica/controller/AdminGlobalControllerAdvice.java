package com.shop.guaridaludica.controller;

import com.shop.guaridaludica.model.Pedido;
import com.shop.guaridaludica.repository.PedidoRepository;

import jakarta.servlet.http.HttpServletRequest;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice(basePackages = "com.shop.guaridaludica.controller")
public class AdminGlobalControllerAdvice {

    @Autowired
    private PedidoRepository pedidoRepository;

    @ModelAttribute("pedidosPendientesCount")
    public long getPedidosPendientesCount() {
        try {
            return pedidoRepository.countByEstado(Pedido.EstadoPedido.PAGADO);
        } catch (Exception e) {
            return 0;
        }
    }

    @ModelAttribute("queryParams")
    public String handleParams(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        request.getParameterMap().forEach((key, values) -> {
            if (!key.equals("page") && values != null) {
                for (String value : values) {
                    sb.append("&")
                      .append(URLEncoder.encode(key, StandardCharsets.UTF_8))
                      .append("=")
                      .append(URLEncoder.encode(value, StandardCharsets.UTF_8));
                }
            }
        });
        return sb.toString(); // Devuelve algo como: "&query=catan&estado=PAGADO"
    }
}
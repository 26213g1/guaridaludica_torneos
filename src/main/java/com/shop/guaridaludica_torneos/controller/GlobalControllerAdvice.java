package com.shop.guaridaludica_torneos.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ModelAttribute
    public void addGlobalAttributes(Model model, HttpSession session) {
        Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
        if (isAdmin == null) {
            isAdmin = true; // Por defecto arrancamos como Admin
            session.setAttribute("isAdmin", isAdmin);
        }
        model.addAttribute("isAdmin", isAdmin);
    }
}
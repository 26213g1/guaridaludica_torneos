package com.shop.guaridaludica_torneos.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DevController {

    @GetMapping("/dev/toggle-admin")
    public String toggleAdmin(HttpSession session, HttpServletRequest request) {
        Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
        if (isAdmin == null) {
            isAdmin = false;
        }
        session.setAttribute("isAdmin", !isAdmin);

        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/torneos");
    }
}
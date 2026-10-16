package com.shop.guaridaludica_torneos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String inicio(Model model) {
        model.addAttribute("template", "home/index"); // Apuntará a templates/home/index.html
        return "layout";
    }
}
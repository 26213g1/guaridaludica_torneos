package com.shop.guaridaludica.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {
    @Autowired
    private ConfiguracionApp configuracionApp;

    @ModelAttribute("appConfig")
    public ConfiguracionApp globalConfig() {
        return configuracionApp;
    }
}

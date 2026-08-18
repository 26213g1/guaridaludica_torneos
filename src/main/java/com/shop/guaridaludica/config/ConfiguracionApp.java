package com.shop.guaridaludica.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "tienda")
public class ConfiguracionApp {

    private String nombre;
    private String moneda;
    private String simboloMoneda;
    private String impuestoPorcentaje;

    // Getters y Setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getMoneda() { return moneda; }
    public void setMoneda(String moneda) { this.moneda = moneda; }

    public String getSimboloMoneda() { return simboloMoneda; }
    public void setSimboloMoneda(String simboloMoneda) { this.simboloMoneda = simboloMoneda; }

    public String getImpuestoPorcentaje() { return impuestoPorcentaje; }
    public void setImpuestoPorcentaje(String impuestoPorcentaje) { this.impuestoPorcentaje = impuestoPorcentaje; }
}
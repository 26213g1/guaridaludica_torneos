package com.shop.guaridaludica.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "tienda")
public class ConfiguracionApp {

    private String nombre;
    private String moneda;
    private String simboloMoneda;
    private String impuestoPorcentaje;
    private int diasProductoNuevo = 7;
    private int marcarUltimasUnidades = 5;
    private boolean mostrarNovedadesHome;
    private boolean mostrarDestacadosHome;
    private String textoImpuesto;
    
    public boolean isMostrarDestacadosHome() {
        return mostrarDestacadosHome;
    }

    public void setMostrarDestacadosHome(boolean mostrarDestacadosHome) {
        this.mostrarDestacadosHome = mostrarDestacadosHome;
    }
    
    public String getTextoImpuesto() {
        return textoImpuesto;
    }

    public void setTextoImpuesto(String textoImpuesto) {
        this.textoImpuesto = textoImpuesto;
    }

    public boolean isMostrarNovedadesHome() {
        return mostrarNovedadesHome;
    }

    public void setMostrarNovedadesHome(boolean mostrarNovedadesHome) {
        this.mostrarNovedadesHome = mostrarNovedadesHome;
    }

    public int getMarcarUltimasUnidades() {
        return marcarUltimasUnidades;
    }

    public void setMarcarUltimasUnidades(int marcarUltimasUnidades) {
        this.marcarUltimasUnidades = marcarUltimasUnidades;
    }

    private String cabeceraInicio;

    public String getCabeceraInicio() {
        return cabeceraInicio;
    }

    public void setCabeceraInicio(String cabeceraInicio) {
        this.cabeceraInicio = cabeceraInicio;
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public String getSimboloMoneda() {
        return simboloMoneda;
    }

    public void setSimboloMoneda(String simboloMoneda) {
        this.simboloMoneda = simboloMoneda;
    }

    public String getImpuestoPorcentaje() {
        return impuestoPorcentaje;
    }

    public void setImpuestoPorcentaje(String impuestoPorcentaje) {
        this.impuestoPorcentaje = impuestoPorcentaje;
    }

    public int getDiasProductoNuevo() {
        return diasProductoNuevo;
    }

    public void setDiasProductoNuevo(int diasProductoNuevo) {
        this.diasProductoNuevo = diasProductoNuevo;
    }

}
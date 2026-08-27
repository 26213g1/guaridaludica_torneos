package com.shop.guaridaludica.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "contacto")
public class ContactoConfiguracion {
    private String email;
    private String telefono;
    private String direccion;
    private Redes redes = new Redes();

    public static class Redes {
        private String instagram;
        private String twitter;
        
        public String getInstagram() {
            return instagram;
        }
        public void setInstagram(String instagram) {
            this.instagram = instagram;
        }
        public String getTwitter() {
            return twitter;
        }
        public void setTwitter(String twitter) {
            this.twitter = twitter;
        }
        
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Redes getRedes() {
        return redes;
    }

    public void setRedes(Redes redes) {
        this.redes = redes;
    }
    
}

package com.shop.guaridaludica_torneos.controller;

import com.shop.guaridaludica_torneos.model.Participacion;
import java.util.List;

public class WrapperParticipaciones {

    private List<Participacion> participaciones;

    public List<Participacion> getParticipaciones() {
        return participaciones;
    }

    public void setParticipaciones(List<Participacion> participaciones) {
        this.participaciones = participaciones;
    }
}
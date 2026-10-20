package com.shop.guaridaludica_torneos.controller;

import com.shop.guaridaludica_torneos.model.Participacion;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class WrapperParticipaciones {

    private List<Participacion> participaciones;
}
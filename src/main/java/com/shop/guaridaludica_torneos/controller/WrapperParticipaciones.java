package com.shop.guaridaludica_torneos.controller;

import com.shop.guaridaludica_torneos.model.Participacion;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class WrapperParticipaciones {

    private List<Participacion> participaciones;
}
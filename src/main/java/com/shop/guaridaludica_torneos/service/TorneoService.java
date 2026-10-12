package com.shop.guaridaludica_torneos.service;

import com.shop.guaridaludica_torneos.model.Torneo;
import com.shop.guaridaludica_torneos.repository.TorneoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class TorneoService {

    private final TorneoRepository torneoRepository;
    private final CloudinaryService cloudinaryService;

    public TorneoService(TorneoRepository torneoRepository, CloudinaryService cloudinaryService) {
        this.torneoRepository = torneoRepository;
        this.cloudinaryService = cloudinaryService;
    }

    public List<Torneo> listarTorneos() {
        return torneoRepository.findAll();
    }

    public Optional<Torneo> obtenerTorneoPorId(Long id) {
        return torneoRepository.findById(id);
    }

    public Torneo guardarTorneo(Torneo torneo, MultipartFile imagen) throws IOException {
        if (imagen != null && !imagen.isEmpty()) {
            String urlImagen = cloudinaryService.uploadFile(imagen);
            torneo.setImagenUrl(urlImagen);
        }
        return torneoRepository.save(torneo);
    }

    public void eliminarTorneo(Long id) {
        torneoRepository.deleteById(id);
    }
}
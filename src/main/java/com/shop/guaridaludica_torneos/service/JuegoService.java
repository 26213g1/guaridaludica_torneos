package com.shop.guaridaludica_torneos.service;

import com.shop.guaridaludica_torneos.model.Juego;
import com.shop.guaridaludica_torneos.repository.JuegoRepository;

//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class JuegoService {

    private final JuegoRepository juegoRepository;

    // @Autowired
    // private CloudinaryService cloudinaryService;
    private final CloudinaryService cloudinaryService;

    public JuegoService(JuegoRepository juegoRepository, CloudinaryService cloudinaryService) {
        this.juegoRepository = juegoRepository;
        this.cloudinaryService = cloudinaryService;
    }

    public List<Juego> listarJuegos() {
        return juegoRepository.findAll();
    }

    public Optional<Juego> obtenerJuegoPorId(Long id) {
        return juegoRepository.findById(id);
    }

    public Juego guardarJuego(Juego juego, MultipartFile imagen) throws IOException {
        // Si se adjunta una imagen, la subimos a Cloudinary y asignamos la URL
        if (imagen != null && !imagen.isEmpty()) {
            String urlImagen = cloudinaryService.uploadFile(imagen);
            juego.setImagenUrl(urlImagen);
        }
        return juegoRepository.save(juego);
    }

    public void eliminarJuego(Long id) {
        juegoRepository.deleteById(id);
    }
}
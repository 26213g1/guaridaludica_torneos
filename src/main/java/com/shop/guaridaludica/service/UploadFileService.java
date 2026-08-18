package com.shop.guaridaludica.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class UploadFileService {

    // Carpeta donde se guardarán las imágenes de los productos
    private final String folder = "src/main/resources/static/images/products/";

    public String saveImage(MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            byte[] bytes = file.getBytes();
            // Generamos un nombre único para evitar que se sobrescriban archivos con el mismo nombre
            String uniqueFilename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path path = Paths.get(folder + uniqueFilename);
            Files.write(path, bytes);
            
            // Retornamos la ruta relativa que se guardará en la BBDD
            return "/images/products/" + uniqueFilename;
        }
        return null;
    }
}
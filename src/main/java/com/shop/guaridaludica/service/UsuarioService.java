package com.shop.guaridaludica.service;

import com.shop.guaridaludica.dto.RegistroUsuarioDto;
import com.shop.guaridaludica.model.Usuario;
import com.shop.guaridaludica.repository.PedidoRepository;
import com.shop.guaridaludica.repository.UsuarioRepository;

import java.util.HashSet;
import java.util.Optional;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // Asegúrate de tenerlo inyectado para encriptar contraseñas

    // Obtener lista de usuarios paginada, con o sin filtro de búsqueda
    public Page<Usuario> obtenerUsuariosPaginados(String keyword, Pageable pageable) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            return usuarioRepository.findByNombreContainingIgnoreCaseOrEmailContainingIgnoreCase(keyword, keyword, pageable);
        }
        return usuarioRepository.findAll(pageable);
    }

    // Comprobar si un usuario tiene pedidos asociados
    public boolean usuarioTienePedidos(Long usuarioId) {
        return pedidoRepository.existsByUsuarioId(usuarioId);
    }

    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public void guardarUsuario(Usuario usuario) {
        usuarioRepository.save(usuario);
    }

    public void actualizarRoles(Long usuarioId, Set<String> nuevosRoles) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        usuario.setRoles(nuevosRoles);
        usuarioRepository.save(usuario);
    }

    public boolean eliminarUsuario(Long id) {
        // Opcional: Podríamos validar si tiene pedidos antes de borrar, 
        // pero por ahora permitimos la acción o lanzamos aviso si se prefiere.
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public void registrarCliente(RegistroUsuarioDto usuarioDto) {
        // 1. Verificar si el correo ya está registrado en la base de datos
        if (usuarioRepository.findByEmail(usuarioDto.getEmail()).isPresent()) {
            throw new RuntimeException("Ya existe una cuenta registrada con este correo electrónico.");
        }

        // 2. Crear la nueva entidad de usuario
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setAlias(usuarioDto.getAlias()); // Guardamos el alias
        nuevoUsuario.setEmail(usuarioDto.getEmail());
    
        // 3. Encriptar la contraseña antes de guardarla
        nuevoUsuario.setPassword(passwordEncoder.encode(usuarioDto.getPassword()));

        // 4. Asignar por defecto el rol de usuario estándar (evitando cuentas huérfanas)
        Set<String> rolesIniciales = new HashSet<>();
        rolesIniciales.add("ROLE_USER");
        nuevoUsuario.setRoles(rolesIniciales); // Ajusta esto según cómo gestione tu entidad los roles (Set<String> o entidad Role)

        // 5. Persistir en la base de datos
        usuarioRepository.save(nuevoUsuario);
    }

}
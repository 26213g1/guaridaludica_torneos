package com.shop.guaridaludica.service;

import com.shop.guaridaludica.model.Usuario;
import com.shop.guaridaludica.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));

        // Mapeamos los roles del Set a autoridades de Spring Security
        List<SimpleGrantedAuthority> authorities = usuario.getRoles().stream().map(SimpleGrantedAuthority::new)
                .toList();

        //return new User(usuario.getEmail(), usuario.getPassword(), authorities);
        return new User(usuario.getAlias(), usuario.getPassword(), authorities);
    }
}
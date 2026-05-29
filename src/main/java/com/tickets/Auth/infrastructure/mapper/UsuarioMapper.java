package com.tickets.Auth.infrastructure.mapper;


import com.tickets.Auth.domain.model.Usuario;
import com.tickets.Auth.infrastructure.driver_adapters.jpa.repository.UsuarioData;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {
    public UsuarioData toUsuarioData(Usuario usuario) {
        return new UsuarioData(
                usuario.getCedula(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getPassword(),
                usuario.getTelefono(),
                usuario.getRol()
        );
    }

    public Usuario toUsuario(UsuarioData usuarioData) {
        return new Usuario(
                usuarioData.getCedula(),
                usuarioData.getNombre(),
                usuarioData.getEmail(),
                usuarioData.getPassword(),
                usuarioData.getTelefono(),
                usuarioData.getRol()
        );
    }
}

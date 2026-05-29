package com.tickets.Auth.infrastructure.driver_adapters.jpa.repository;


import com.tickets.Auth.domain.model.Usuario;
import com.tickets.Auth.domain.model.gateway.UsuarioGateway;
import com.tickets.Auth.infrastructure.mapper.UsuarioMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UsuarioDataGatewayImpl implements UsuarioGateway {
    private final UsuarioDataJpaRepository usuarioDataJpaRepository;
    private final UsuarioMapper usuarioMapper;

    @Override
    public Usuario guardarUsuario(Usuario usuario) {
        UsuarioData usuarioDataGuardar = usuarioMapper.toUsuarioData(usuario);
        return usuarioMapper.toUsuario(usuarioDataJpaRepository.save(usuarioDataGuardar));
    }

    @Override
    public Usuario buscarUsuario(String cedula) {
        return null;
    }

    @Override
    public void eliminarUsuario(String cedula) {

    }

    @Override
    public Usuario buscarEmail(String email) {
        return null;
    }

    @Override
    public Usuario actualizarUsuario(Usuario usuario) {
        return null;
    }
}

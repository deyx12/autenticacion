package com.tickets.Auth.domain.model.gateway;


import com.tickets.Auth.domain.model.Usuario;

public interface UsuarioGateway {
    Usuario guardarUsuario(Usuario usuario);
    Usuario buscarUsuario(String cedula);
    void eliminarUsuario(String cedula);
    Usuario buscarEmail(String email);
    Usuario actualizarUsuario(Usuario usuario);
}

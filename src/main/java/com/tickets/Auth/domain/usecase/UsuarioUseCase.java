package com.tickets.Auth.domain.usecase;

import com.tickets.Auth.domain.model.Usuario;
import com.tickets.Auth.domain.model.gateway.EncryptGateway;
import com.tickets.Auth.domain.model.gateway.UsuarioGateway;
import com.tickets.Auth.infrastructure.security.JwtService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor

public class UsuarioUseCase {
    private final UsuarioGateway usuarioGateway;
    private final EncryptGateway encryptGateway;
    private final JwtService jwtService;

    public Usuario guardarUsuario(Usuario usuario) {
        if(usuario.getCedula() == null || usuario.getCedula().trim().isEmpty()) {
            throw new RuntimeException("La cedula es obligatoria");
        }
        try {
            usuarioGateway.buscarUsuario(usuario.getCedula());
            throw new RuntimeException("El ususario ya está registrado");
        } catch (RuntimeException error) {
            if(!error.getMessage().equals("Usuario No encontrado")) {
                throw error;
            }
        }

        if (usuario.getNombre() == null || usuario.getNombre().trim().isEmpty()) {
            throw new RuntimeException("El nombre es obligatorio");
        }
        if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
            throw new RuntimeException("El email es obligatorio");
        }
        if (usuario.getPassword() == null || usuario.getPassword().trim().isEmpty() ) {
            throw new RuntimeException("La clave es obligatoria");
        }
        if (usuario.getTelefono() == null || usuario.getTelefono().trim().isEmpty() ) {
            throw new RuntimeException("El telefono es obligatorio");
        }
        if (usuario.getRol() == null || usuario.getRol().trim().isEmpty() ) {
            throw new RuntimeException("El rol es obligatorio");
        }

        String passEncrypt = encryptGateway.encrypter(usuario.getPassword());
        usuario.setPassword(passEncrypt);

        return usuarioGateway.guardarUsuario(usuario);

    }

    public Usuario buscarUsuario(String cedula) {
        try {
            return usuarioGateway.buscarUsuario(cedula);
        } catch (RuntimeException error) {
            throw new RuntimeException("Usuario No encontrado");
        }
    }

    public void eliminarUsuario(String cedula) {
        buscarUsuario(cedula);
        usuarioGateway.eliminarUsuario(cedula);
    }

    public Usuario actualizarUsuario(String cedula, Usuario usuario) {
        Usuario usuarioExistente = buscarUsuario(cedula);
        if (usuario.getNombre() != null && !usuario.getNombre().trim().isEmpty()) {
            usuarioExistente.setNombre(usuario.getNombre());
        }
//        else {
//            throw new RuntimeException("El nombre es obligatorio");
//        }
        if (usuario.getEmail() != null && !usuario.getEmail().trim().isEmpty()) {
            usuarioExistente.setEmail(usuario.getEmail());
        }
//        else {
//            throw new RuntimeException("La descripción es obligatoria");
//        }
        if (usuario.getTelefono() != null && !usuario.getTelefono().trim().isEmpty()) {
            usuarioExistente.setTelefono(usuario.getTelefono());
        }
//        else {
//            throw new RuntimeException("La descripción es obligatoria");
//        }
        if (usuario.getRol() != null && !usuario.getRol().trim().isEmpty()) {
            usuarioExistente.setRol(usuario.getRol());
        }
//        else {
//            throw new RuntimeException("La descripción es obligatoria");
//        }

        return usuarioGateway.guardarUsuario(usuarioExistente);
    }

    public String login(String email, String password) {
        try {
            Usuario usuario = usuarioGateway.buscarEmail(email);
            Boolean clave = encryptGateway.encrypterV(password, usuario.getPassword());
            if (clave == true) {
                return jwtService.generarToken(usuario);
                //return "Login Exitoso";
            }
            else {
                return "Credenciales invalidas";
            }
        } catch (RuntimeException error) {
            throw error;
        }
    }

    public String cambiarPassword(String email, String passwordActual, String passwordNueva) {

        Usuario usuario = usuarioGateway.buscarEmail(email);

        Boolean claveValida = encryptGateway.encrypterV(passwordActual, usuario.getPassword());

        if (!Boolean.TRUE.equals(claveValida)) {
            throw new RuntimeException("La contraseña actual es incorrecta");
        }

        if (passwordNueva == null || passwordNueva.trim().isEmpty()) {
            throw new RuntimeException("La nueva contraseña es obligatoria");
        }

        if (passwordActual.equals(passwordNueva)) {
            throw new RuntimeException("La nueva contraseña no puede ser igual a la actual");
        }

        String passwordEncriptada = encryptGateway.encrypter(passwordNueva);

        usuario.setPassword(passwordEncriptada);

        usuarioGateway.guardarUsuario(usuario);

        return "Contraseña actualizada correctamente";
    }
}

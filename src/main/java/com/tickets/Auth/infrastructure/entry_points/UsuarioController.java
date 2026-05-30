package com.tickets.Auth.infrastructure.entry_points;

import com.tickets.Auth.domain.usecase.UsuarioUseCase;
import com.tickets.Auth.infrastructure.mapper.UsuarioMapper;
import com.tickets.Auth.infrastructure.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/tickets/usuario")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioUseCase usuarioUseCase;
    private final UsuarioMapper usuarioMapper;
    private final JwtService jwtService;

    @PostMapping("/validar-token")
    public Map<String, Object> validarToken(@RequestHeader("Authorization") String authorizationHeader) {

        String token = authorizationHeader.replace("Bearer ", "");

        boolean esValido = jwtService.validarToken(token);

        if (!esValido) {
            throw new RuntimeException("Token inválido");
        }

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("valido", true);
        respuesta.put("email", jwtService.obtenerEmail(token));
        respuesta.put("cedula", jwtService.obtenerCedula(token));
        respuesta.put("rol", jwtService.obtenerRol(token));

        return respuesta;
    }

    @PostMapping("/cambiar-password")
    public String cambiarPassword(@RequestBody Map<String, String> datos) {

        String email = datos.get("email");
        String passwordActual = datos.get("passwordActual");
        String passwordNueva = datos.get("passwordNueva");

        return usuarioUseCase.cambiarPassword(email, passwordActual, passwordNueva);
    }
}

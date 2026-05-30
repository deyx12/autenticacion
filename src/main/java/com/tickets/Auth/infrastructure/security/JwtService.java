package com.tickets.Auth.infrastructure.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.tickets.Auth.domain.model.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String secret;

    public String generarToken(Usuario usuario) {
        return JWT.create()
                .withSubject(usuario.getEmail())
                .withClaim("cedula", usuario.getCedula())
                .withClaim("rol", usuario.getRol())
                .withExpiresAt(new Date(System.currentTimeMillis() + 3600000))
                .sign(Algorithm.HMAC256(secret));
    }

    public boolean validarToken(String token) {
        try {
            JWT.require(Algorithm.HMAC256(secret))
                    .build()
                    .verify(token);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String obtenerEmail(String token) {
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(secret))
                .build()
                .verify(token);

        return decodedJWT.getSubject();
    }

    public String obtenerRol(String token) {
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(secret))
                .build()
                .verify(token);

        return decodedJWT.getClaim("rol").asString();
    }

    public String obtenerCedula(String token) {
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(secret))
                .build()
                .verify(token);

        return decodedJWT.getClaim("cedula").asString();
    }
}

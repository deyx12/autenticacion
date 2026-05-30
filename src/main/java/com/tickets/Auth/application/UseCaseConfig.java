package com.tickets.Auth.application;


import com.tickets.Auth.domain.model.gateway.EncryptGateway;
import com.tickets.Auth.domain.model.gateway.UsuarioGateway;
import com.tickets.Auth.domain.usecase.UsuarioUseCase;
import com.tickets.Auth.infrastructure.security.JwtService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public UsuarioUseCase usuarioUseCase(UsuarioGateway usuarioGateway, EncryptGateway encryptGateway, JwtService jwtService) {
        return new UsuarioUseCase(usuarioGateway, encryptGateway, jwtService);
    }
}

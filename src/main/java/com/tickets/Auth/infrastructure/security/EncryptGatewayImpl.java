package com.tickets.Auth.infrastructure.security;

import com.tickets.Auth.domain.model.gateway.EncryptGateway;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class EncryptGatewayImpl implements EncryptGateway {
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public String encrypter(String password) {
        return encoder.encode(password);
    }

    @Override
    public Boolean encrypterV(String password, String passwordB) {
        return encoder.matches(password, passwordB);
    }
}

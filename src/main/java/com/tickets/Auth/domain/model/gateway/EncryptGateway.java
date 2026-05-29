package com.tickets.Auth.domain.model.gateway;

public interface EncryptGateway {
    String encrypter(String password);
    Boolean encrypterV(String password, String passwordB);
}

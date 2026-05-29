package com.tickets.Auth.infrastructure.driver_adapters.jpa.repository;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="Usuarios")
@Data

public class UsuarioData {
    @Id
    private String cedula;
    private String nombre;
    private String email;
    private String password;
    @Column(length = 10)
    private String telefono;
    private String rol;

}

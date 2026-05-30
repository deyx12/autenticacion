package com.tickets.Auth.infrastructure.entry_points;

import com.tickets.Auth.domain.model.Usuario;
import com.tickets.Auth.domain.usecase.UsuarioUseCase;
import com.tickets.Auth.infrastructure.driver_adapters.jpa.repository.UsuarioData;
import com.tickets.Auth.infrastructure.mapper.UsuarioMapper;
import com.tickets.Auth.infrastructure.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/save")
    public ResponseEntity<?> saveUsuario(@RequestBody UsuarioData usuarioData) {
        try {
            Usuario usuarioValidadoGuardado = usuarioUseCase.guardarUsuario(usuarioMapper.toUsuario(usuarioData));
            if(usuarioValidadoGuardado.getCedula() != null) {
                return new ResponseEntity<>(usuarioValidadoGuardado, HttpStatus.OK);
            }
            return new ResponseEntity<>(usuarioValidadoGuardado, HttpStatus.CONFLICT);
        } catch (RuntimeException error) {
            if ("El ususario ya está registrado".equals(error.getMessage())) {
                return new ResponseEntity<>(error.getMessage(), HttpStatus.CONFLICT);
            }

            return new ResponseEntity<>(error.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{cedula}")
    public ResponseEntity<?> findByCedulaUsuario(@PathVariable String cedula) {
        try {
            Usuario usuarioValidadoEncontrado = usuarioUseCase.buscarUsuario(cedula);

            if(usuarioValidadoEncontrado.getCedula() != null) {
                return new ResponseEntity<>(usuarioValidadoEncontrado, HttpStatus.OK);
            }
            return new ResponseEntity<>(usuarioValidadoEncontrado, HttpStatus.OK);
        } catch (RuntimeException error) {
            return new ResponseEntity<>(error.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{cedula}")
    public ResponseEntity<?> deleteByCedula(@PathVariable String cedula) {
        try {
            usuarioUseCase.eliminarUsuario(cedula);
            return ResponseEntity.ok().body("Usuario eliminado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }

    @PatchMapping("/{cedula}")
    public ResponseEntity<?> actualizar(@PathVariable String cedula, @RequestBody UsuarioData usuarioData) {
        try {
            Usuario actualizado = usuarioUseCase.actualizarUsuario(cedula, usuarioMapper.toUsuario(usuarioData));
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UsuarioData usuarioData) {
        try{
            String mensaje = usuarioUseCase.login(usuarioData.getEmail(), usuarioData.getPassword());
            return new ResponseEntity<>(mensaje, HttpStatus.OK);
        } catch (RuntimeException error) {
            return new ResponseEntity<>(error.getMessage(), HttpStatus.OK);
        }
    }

    @PostMapping("/validar-token")
    public ResponseEntity<?> validarToken(@RequestHeader("Authorization") String authorizationHeader) {

        String token = authorizationHeader.replace("Bearer ", "");

        boolean esValido = jwtService.validarToken(token);

        if (!esValido) {
            return new ResponseEntity<>("Token invalido", HttpStatus.UNAUTHORIZED);
        }

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("valido", true);
        respuesta.put("email", jwtService.obtenerEmail(token));
        respuesta.put("cedula", jwtService.obtenerCedula(token));
        respuesta.put("rol", jwtService.obtenerRol(token));

        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }

    @PostMapping("/cambiar-password")
    public ResponseEntity<?> cambiarPassword(@RequestBody Map<String, String> datos) {

        String email = datos.get("email");
        String passwordActual = datos.get("passwordActual");
        String passwordNueva = datos.get("passwordNueva");

        try {
            String mensaje = usuarioUseCase.cambiarPassword(email, passwordActual, passwordNueva);
            return ResponseEntity.ok(mensaje);
        } catch (RuntimeException error) {
            String mensaje = error.getMessage();

            if ("Correo No encontrado".equals(mensaje)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensaje);
            }

            if ("La contraseña actual es incorrecta".equals(mensaje)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(mensaje);
            }

            return ResponseEntity.badRequest().body(mensaje);
        }
    }
}

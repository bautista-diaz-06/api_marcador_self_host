package com.proyecto.marcador_mapa.controllers.auth;

import com.proyecto.marcador_mapa.dto.request.LoginRequestDTO;
import com.proyecto.marcador_mapa.dto.request.RegisterRequestDTO;
import com.proyecto.marcador_mapa.dto.response.UserResponseDTO;
import com.proyecto.marcador_mapa.services.auth.AuthServices;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthServices authServices;

    public AuthController(AuthServices userServices) {
        this.authServices = userServices;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody RegisterRequestDTO data) {
        return ResponseEntity.ok(this.authServices.register(data));
    }

    @PostMapping("/login")
    public ResponseEntity login(@Valid @RequestBody LoginRequestDTO data) {
        this.authServices.login(data);
        return ResponseEntity.ok().build();
    }
}

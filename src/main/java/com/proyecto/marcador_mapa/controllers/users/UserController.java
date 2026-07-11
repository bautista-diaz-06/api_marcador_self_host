package com.proyecto.marcador_mapa.controllers.users;

import com.proyecto.marcador_mapa.dto.response.UserResponseDTO;
import com.proyecto.marcador_mapa.services.userServices.UserServices;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserServices userServices;

    public UserController(UserServices userServices) {
        this.userServices = userServices;
    }

    @GetMapping("/")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers(){
        return ResponseEntity.ok(this.userServices.getAllUsers());
    };
}

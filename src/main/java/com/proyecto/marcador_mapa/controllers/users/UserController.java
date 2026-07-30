package com.proyecto.marcador_mapa.controllers.users;

import com.proyecto.marcador_mapa.dto.request.UserRequestDTO;
import com.proyecto.marcador_mapa.dto.response.UserResponseDTO;
import com.proyecto.marcador_mapa.entities.Users;
import com.proyecto.marcador_mapa.services.userServices.UserServices;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id){
        this.userServices.deleteUser(id);
    }

    @GetMapping("/me")
    // Spring Security inyecta el usuario autenticado en el parámetro Authentication.
    // getPrincipal() devuelve el principal autenticado (Users).
    public ResponseEntity<UserResponseDTO> getMyInfo(Authentication authentication){
        Users user = (Users) authentication.getPrincipal();
        return ResponseEntity.ok(this.userServices.myInfo(user));
    }

    @PutMapping("/")
    public ResponseEntity<UserResponseDTO> updateUser(@Valid @RequestBody UserRequestDTO user, Authentication authentication){
        Users users = (Users) authentication.getPrincipal();
        return ResponseEntity.ok(this.userServices.updateUser(user, users));
    }
}

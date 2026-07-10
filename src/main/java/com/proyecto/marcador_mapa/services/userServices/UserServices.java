package com.proyecto.marcador_mapa.services.userServices;

import com.proyecto.marcador_mapa.dto.response.UserResponseDTO;

import java.util.List;

public interface UserServices {
    List<UserResponseDTO> getAllUsers();
    UserResponseDTO getUserByEmail(String email);
    void deleteUser(Long id);
}

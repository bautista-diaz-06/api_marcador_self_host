package com.proyecto.marcador_mapa.services.userServices;

import com.proyecto.marcador_mapa.dto.request.UserRequestDTO;
import com.proyecto.marcador_mapa.dto.response.UserResponseDTO;

import java.util.List;

public interface UserServices {
    List<UserResponseDTO> getAllUsers();
    UserResponseDTO getUserByName(String name);
    void deleteUser(Long id);
}

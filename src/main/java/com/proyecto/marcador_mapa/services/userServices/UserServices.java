package com.proyecto.marcador_mapa.services.userServices;

import com.proyecto.marcador_mapa.dto.request.UserRequestDTO;
import com.proyecto.marcador_mapa.dto.response.UserResponseDTO;
import com.proyecto.marcador_mapa.entities.Users;

import java.util.List;
import java.util.Optional;

public interface UserServices {
    List<UserResponseDTO> getAllUsers();
    //Este ByEmail lo utiliza el Custom del JWT
    UserResponseDTO getUserByEmail(String email);
    void deleteUser(Long id);
    UserResponseDTO myInfo(Users user);
    UserResponseDTO updateUser(UserRequestDTO user, Users users);
}

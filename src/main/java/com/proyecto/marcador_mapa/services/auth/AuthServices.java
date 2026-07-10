package com.proyecto.marcador_mapa.services.auth;

import com.proyecto.marcador_mapa.dto.request.LoginRequestDTO;
import com.proyecto.marcador_mapa.dto.request.RegisterRequestDTO;
import com.proyecto.marcador_mapa.dto.response.UserResponseDTO;

public interface AuthServices {
    UserResponseDTO register(RegisterRequestDTO data);
    void login(LoginRequestDTO loginData);
}

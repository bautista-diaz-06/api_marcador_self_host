package com.proyecto.marcador_mapa.services.auth;

import com.proyecto.marcador_mapa.dto.request.LoginRequestDTO;
import com.proyecto.marcador_mapa.dto.request.RefreshTokenRequestDTO;
import com.proyecto.marcador_mapa.dto.request.RegisterRequestDTO;
import com.proyecto.marcador_mapa.dto.response.AuthResponseDTO;
import com.proyecto.marcador_mapa.dto.response.UserResponseDTO;

public interface AuthServices {
    UserResponseDTO register(RegisterRequestDTO data);
    AuthResponseDTO login(LoginRequestDTO loginData);
    AuthResponseDTO refreshToken(RefreshTokenRequestDTO refreshData);
}

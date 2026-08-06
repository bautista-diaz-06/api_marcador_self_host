package com.proyecto.marcador_mapa.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RefreshTokenRequestDTO {
    // Recibe el refresh token emitido durante el login previo.
    @NotBlank(message = "El refresh token es obligatorio.")
    private String refreshToken;
}

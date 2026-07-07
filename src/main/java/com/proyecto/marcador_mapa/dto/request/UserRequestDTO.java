package com.proyecto.marcador_mapa.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserRequestDTO {
    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(min = 3, max = 30, message = "El nombre de usuario debe tener entre 3 y 30 caracteres")
    private String username;

    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "El correo electrónico no es válido")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 4, max = 100, message = "La contraseña debe tener al menos 4 caracteres")
    private String password;
}

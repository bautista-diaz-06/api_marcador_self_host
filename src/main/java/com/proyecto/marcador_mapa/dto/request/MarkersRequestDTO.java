package com.proyecto.marcador_mapa.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MarkersRequestDTO {

    @NotBlank(message = "El título es obligatorio.")
    private String title;

    private String description;

    @NotNull(message = "La latitud es obligatoria.")
    @DecimalMin(value = "-90.0", message = "La latitud debe ser mayor o igual a -90.")
    @DecimalMax(value = "90.0", message = "La latitud debe ser menor o igual a 90.")
    private BigDecimal latitude;

    @NotNull(message = "La longitud es obligatoria.")
    @DecimalMin(value = "-180.0", message = "La longitud debe ser mayor o igual a -180.")
    @DecimalMax(value = "180.0", message = "La longitud debe ser menor o igual a 180.")
    private BigDecimal longitude;
}

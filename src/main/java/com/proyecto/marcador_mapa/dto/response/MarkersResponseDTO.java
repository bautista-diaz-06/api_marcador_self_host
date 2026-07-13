package com.proyecto.marcador_mapa.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MarkersResponseDTO {
    private Long id;

    private String title;

    private String description;

    private BigDecimal latitude;

    private BigDecimal longitude;
}

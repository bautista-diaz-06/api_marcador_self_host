package com.proyecto.marcador_mapa.mapper.markers;

import com.proyecto.marcador_mapa.dto.request.MarkersRequestDTO;
import com.proyecto.marcador_mapa.dto.response.MarkersResponseDTO;
import com.proyecto.marcador_mapa.entities.Markers;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MarkerMapper {

    Markers toEntity(MarkersRequestDTO marker);

    MarkersResponseDTO toResponse(Markers marker);
}

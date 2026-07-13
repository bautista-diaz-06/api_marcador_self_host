package com.proyecto.marcador_mapa.services.markersServices;

import com.proyecto.marcador_mapa.dto.request.MarkersRequestDTO;
import com.proyecto.marcador_mapa.dto.response.MarkersResponseDTO;

import java.util.List;

public interface MarkersServices {
    MarkersResponseDTO createMarker(MarkersRequestDTO marker);
    List<MarkersResponseDTO> getAllMarkers();
    List<MarkersResponseDTO> getMarkersByUser(Long id);
}

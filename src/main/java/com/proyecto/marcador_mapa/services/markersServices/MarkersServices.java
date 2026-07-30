package com.proyecto.marcador_mapa.services.markersServices;

import com.proyecto.marcador_mapa.dto.request.MarkersRequestDTO;
import com.proyecto.marcador_mapa.dto.response.MarkersResponseDTO;
import com.proyecto.marcador_mapa.entities.Users;

import java.util.List;

public interface MarkersServices {
    MarkersResponseDTO createMarker(MarkersRequestDTO marker, Users user);
    List<MarkersResponseDTO> getAllMarkers();
    List<MarkersResponseDTO> getMyMarkers(Users user);
    void deleteMarker(Long id, Users user);
    MarkersResponseDTO updateMarker(MarkersRequestDTO marker, Long id, Users user);
}

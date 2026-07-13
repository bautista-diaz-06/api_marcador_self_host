package com.proyecto.marcador_mapa.services.markersServices;

import com.proyecto.marcador_mapa.dto.request.MarkersRequestDTO;
import com.proyecto.marcador_mapa.dto.response.MarkersResponseDTO;
import com.proyecto.marcador_mapa.entities.Markers;
import com.proyecto.marcador_mapa.entities.Users;
import com.proyecto.marcador_mapa.mapper.markers.MarkerMapper;
import com.proyecto.marcador_mapa.repository.markersRepository.MarkersRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class MarkersServicesImpl implements MarkersServices {

    private final MarkersRepository markersRepository;
    private final MarkerMapper markerMapper;

    public MarkersServicesImpl(MarkersRepository markersRepository, MarkerMapper markerMapper) {
        this.markersRepository = markersRepository;
        this.markerMapper = markerMapper;
    }

    @Override
    @Transactional
    public MarkersResponseDTO createMarker(MarkersRequestDTO marker) {

        Markers createdMarker = markerMapper.toEntity(marker);
        Markers markerSaved = markersRepository.save(createdMarker);

        return markerMapper.toResponse(markerSaved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MarkersResponseDTO> getAllMarkers() {
        return List.of();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MarkersResponseDTO> getMarkersByUser(Long id) {
        List<MarkersResponseDTO> isMarkerOfUserExists = markersRepository.findByUserId(id)
                .stream()
                .map(markerMapper::toResponse)
                .toList();

        return isMarkerOfUserExists;
    }
}

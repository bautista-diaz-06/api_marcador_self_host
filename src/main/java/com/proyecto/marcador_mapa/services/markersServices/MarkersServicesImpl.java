package com.proyecto.marcador_mapa.services.markersServices;

import com.proyecto.marcador_mapa.dto.request.MarkersRequestDTO;
import com.proyecto.marcador_mapa.dto.response.MarkersResponseDTO;
import com.proyecto.marcador_mapa.entities.Markers;
import com.proyecto.marcador_mapa.entities.Users;
import com.proyecto.marcador_mapa.mapper.markers.MarkerMapper;
import com.proyecto.marcador_mapa.repository.markersRepository.MarkersRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MarkersServicesImpl implements MarkersServices {

    private final MarkersRepository markersRepository;
    private final MarkerMapper markerMapper;

    public MarkersServicesImpl(MarkersRepository markersRepository, MarkerMapper markerMapper) {
        this.markersRepository = markersRepository;
        this.markerMapper = markerMapper;
    }

    @Override
    @Transactional
    public MarkersResponseDTO createMarker(MarkersRequestDTO marker, Users user) {

        Markers createdMarker = markerMapper.toEntity(marker);
        createdMarker.setUser(user);

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
    public List<MarkersResponseDTO> getMyMarkers(Users user) {
        List<MarkersResponseDTO> isMarkerOfUserExists = markersRepository.findByUserId(user.getId())
                .stream()
                .map(markerMapper::toResponse)
                .toList();

        return isMarkerOfUserExists;
    }

    @Override
    @Transactional
    public void deleteMarker(Long id, Users user) {
        Markers existingMarker = this.markersRepository.findById(id).orElseThrow();

        if (!existingMarker.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("No podés eliminar este marcador");
        }

        this.markersRepository.delete(existingMarker);
    }

    @Override
    @Transactional
    public MarkersResponseDTO updateMarker(MarkersRequestDTO marker, Long id, Users user) {
        Markers existingMarker = this.markersRepository.findById(id).orElseThrow();

        if (!existingMarker.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("No podés modificar este marcador");
        }

        existingMarker.setTitle(marker.getTitle());
        existingMarker.setDescription(marker.getDescription());
        existingMarker.setLatitude(marker.getLatitude());
        existingMarker.setLongitude(marker.getLongitude());

        Markers updatedMarker = this.markersRepository.save(existingMarker);
        return markerMapper.toResponse(updatedMarker);
    }
}

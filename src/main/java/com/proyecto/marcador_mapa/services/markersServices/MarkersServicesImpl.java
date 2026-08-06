package com.proyecto.marcador_mapa.services.markersServices;

import com.proyecto.marcador_mapa.dto.request.MarkersRequestDTO;
import com.proyecto.marcador_mapa.dto.response.MarkersResponseDTO;
import com.proyecto.marcador_mapa.entities.Markers;
import com.proyecto.marcador_mapa.entities.Users;
import com.proyecto.marcador_mapa.mapper.markers.MarkerMapper;
import com.proyecto.marcador_mapa.repository.markersRepository.MarkersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
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
        log.info("Creando marcador para usuario id={}", user.getId());

        Markers createdMarker = markerMapper.toEntity(marker);
        createdMarker.setUser(user);

        Markers markerSaved = markersRepository.save(createdMarker);
        log.info("Marcador creado id={} para usuario id={}", markerSaved.getId(), user.getId());

        return markerMapper.toResponse(markerSaved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MarkersResponseDTO> getAllMarkers() {
        List<MarkersResponseDTO> markers = markersRepository.findAll()
                .stream()
                .map(markerMapper::toResponse)
                .toList();
        log.debug("Se obtuvieron {} marcadores totales", markers.size());
        return markers;
    }

    @Override
    @Transactional(readOnly = true)
    public List<MarkersResponseDTO> getMyMarkers(Users user) {
        List<MarkersResponseDTO> myMarkers = markersRepository.findByUserId(user.getId())
                .stream()
                .map(markerMapper::toResponse)
                .toList();
        log.debug("Usuario id={} consultó sus marcadores. Total={}", user.getId(), myMarkers.size());

        return myMarkers;
    }

    @Override
    @Transactional
    public void deleteMarker(Long id, Users user) {
        Markers existingMarker = this.markersRepository.findById(id).orElseThrow();
        if (!existingMarker.getUser().getId().equals(user.getId())) {
            log.warn(
                    "Acceso denegado para eliminar marcador id={}. ownerId={}, requesterId={}",
                    existingMarker.getId(),
                    existingMarker.getUser().getId(),
                    user.getId()
            );
            throw new AccessDeniedException("No podés eliminar este marcador");
        }

        this.markersRepository.delete(existingMarker);
        log.info("Marcador id={} eliminado por usuario id={}", id, user.getId());
    }

    @Override
    @Transactional
    public MarkersResponseDTO updateMarker(MarkersRequestDTO marker, Long id, Users user) {
        Markers existingMarker = this.markersRepository.findById(id).orElseThrow();
        if (!existingMarker.getUser().getId().equals(user.getId())) {
            log.warn(
                    "Acceso denegado para modificar marcador id={}. ownerId={}, requesterId={}",
                    existingMarker.getId(),
                    existingMarker.getUser().getId(),
                    user.getId()
            );
            throw new AccessDeniedException("No podés modificar este marcador");
        }

        existingMarker.setTitle(marker.getTitle());
        existingMarker.setDescription(marker.getDescription());
        existingMarker.setLatitude(marker.getLatitude());
        existingMarker.setLongitude(marker.getLongitude());

        Markers updatedMarker = this.markersRepository.save(existingMarker);
        log.info("Marcador id={} actualizado por usuario id={}", id, user.getId());
        return markerMapper.toResponse(updatedMarker);
    }
}

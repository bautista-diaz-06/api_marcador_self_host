package com.proyecto.marcador_mapa.controllers.markers;

import com.proyecto.marcador_mapa.dto.request.MarkersRequestDTO;
import com.proyecto.marcador_mapa.dto.response.MarkersResponseDTO;
import com.proyecto.marcador_mapa.entities.Users;
import com.proyecto.marcador_mapa.services.markersServices.MarkersServices;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/markers")
public class MarkerController {
    private final MarkersServices markersServices;

    public MarkerController(MarkersServices markersServices) {
        this.markersServices = markersServices;
    }

    @GetMapping("/")
    public ResponseEntity<List<MarkersResponseDTO>> getAllMarkers() {
        return ResponseEntity.ok(this.markersServices.getAllMarkers());
    }

    @PostMapping("/")
    public ResponseEntity<MarkersResponseDTO> createMarker(@Valid @RequestBody MarkersRequestDTO marker, Authentication authentication) {
        Users user = (Users) authentication.getPrincipal();
        return ResponseEntity.ok(this.markersServices.createMarker(marker, user));
    }

    @GetMapping("/me")
    public ResponseEntity<List<MarkersResponseDTO>> getMyMarkers(Authentication authentication){
        Users user = (Users) authentication.getPrincipal();
        return ResponseEntity.ok(this.markersServices.getMyMarkers(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMarker(@PathVariable Long id, Authentication authentication){
        Users user = (Users) authentication.getPrincipal();
        this.markersServices.deleteMarker(id, user);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<MarkersResponseDTO> updateMarker(@Valid @RequestBody MarkersRequestDTO marker, @PathVariable Long id, Authentication authentication){
        Users user = (Users) authentication.getPrincipal();
        return ResponseEntity.ok(this.markersServices.updateMarker(marker, id, user));
    }
}

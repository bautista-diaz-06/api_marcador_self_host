package com.proyecto.marcador_mapa.repository.markersRepository;

import com.proyecto.marcador_mapa.entities.Markers;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MarkersRepository extends JpaRepository<Markers, Long> {
    List<Markers> findByUserId(Long userId);
}

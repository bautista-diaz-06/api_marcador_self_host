package com.proyecto.marcador_mapa.repository.userRepository;

import com.proyecto.marcador_mapa.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {
    Users findByUsername(String username);
}

package com.proyecto.marcador_mapa.mapper.users;

import com.proyecto.marcador_mapa.dto.request.RegisterRequestDTO;
import com.proyecto.marcador_mapa.dto.response.UserResponseDTO;
import com.proyecto.marcador_mapa.entities.Users;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    Users toEntity(RegisterRequestDTO userRequestDTO);

    UserResponseDTO toResponseDTO(Users user);
}

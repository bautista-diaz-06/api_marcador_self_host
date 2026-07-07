package com.proyecto.marcador_mapa.mapper.users;

import com.proyecto.marcador_mapa.dto.request.UserRequestDTO;
import com.proyecto.marcador_mapa.dto.response.UserResponseDTO;
import com.proyecto.marcador_mapa.entities.Users;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    Users toEntity(UserRequestDTO userRequestDTO);

    UserResponseDTO toResponseDTO(Users user);
}

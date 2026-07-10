package com.proyecto.marcador_mapa.services.userServices;

import com.proyecto.marcador_mapa.dto.response.UserResponseDTO;
import com.proyecto.marcador_mapa.entities.Users;
import com.proyecto.marcador_mapa.mapper.users.UserMapper;
import com.proyecto.marcador_mapa.repository.userRepository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServicesImpl implements UserServices {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServicesImpl(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> getAllUsers() {
        List<Users> listOfUsers = this.userRepository.findAll();

        if (listOfUsers.isEmpty()) {
            //mensaje de que está vacia
            return List.of();
        }

        return List.of();
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO getUserByEmail(String email) {

        Users userFinded = this.userRepository.findByEmail(email);

        if (userFinded == null){
            //retornar que no existe
        }

        return userMapper.toResponseDTO(userFinded);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        Users existingUser = this.userRepository.findById(id).orElseThrow();

        this.userRepository.delete(existingUser);
    }
}

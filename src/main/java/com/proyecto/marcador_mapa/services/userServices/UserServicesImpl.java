package com.proyecto.marcador_mapa.services.userServices;

import com.proyecto.marcador_mapa.dto.request.UserRequestDTO;
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
        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponseDTO)
                .toList();
    }

    //Esto es utilizado por el JWT para comprobar el custom
    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO getUserByEmail(String email) {

        Users userFinded = this.userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));;

        return userMapper.toResponseDTO(userFinded);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        Users existingUser = this.userRepository.findById(id).orElseThrow();

        this.userRepository.delete(existingUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO myInfo(Users user) {
        return userMapper.toResponseDTO(user);
    }

    @Override
    @Transactional
    public UserResponseDTO updateUser(UserRequestDTO user, Users users) {
        users.setUsername(user.getUsername());
        users.setEmail(user.getEmail());

        //la entity Users es mutable asi que lo podemos guardar
        Users userUpdated = this.userRepository.save(users);

        UserResponseDTO response = userMapper.toResponseDTO(userUpdated);
        return response;
    }


}

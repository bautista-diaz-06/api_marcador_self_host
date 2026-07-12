package com.proyecto.marcador_mapa.services.auth;

import com.proyecto.marcador_mapa.dto.request.LoginRequestDTO;
import com.proyecto.marcador_mapa.dto.request.RegisterRequestDTO;
import com.proyecto.marcador_mapa.dto.response.AuthResponseDTO;
import com.proyecto.marcador_mapa.dto.response.UserResponseDTO;
import com.proyecto.marcador_mapa.entities.Users;
import com.proyecto.marcador_mapa.mapper.users.UserMapper;
import com.proyecto.marcador_mapa.repository.userRepository.UserRepository;
import com.proyecto.marcador_mapa.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServicesImpl implements AuthServices {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthServicesImpl(PasswordEncoder passwordEncoder, UserRepository userRepository, UserMapper userMapper, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Override
    public UserResponseDTO register(RegisterRequestDTO data) {
        /**
         * En la variable que es entidad, guardamos como valor el mapeo del data que viene como parametro
         *         a un entity, luego a esa entity la guardamos, y esa entity con el valor de lo guardado se mapea a
         *         el DTO de response para mostrarlo
         */

        Users userCreated = userMapper.toEntity(data);

        //Como es mutable entonces hasheamos la contraseña y se guarda despues
        userCreated.setPassword(passwordEncoder.encode(data.getPassword()));

        Users userSaved = this.userRepository.save(userCreated);
        UserResponseDTO response = userMapper.toResponseDTO(userSaved);
        return response;
    }

    @Override
    public AuthResponseDTO login(LoginRequestDTO loginData) {

        System.out.println("DTO completo: " + loginData);
        System.out.println("Email recibido: " + loginData.getEmail());
        System.out.println("Password recibido: " + loginData.getPassword());

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginData.getEmail(),
                loginData.getPassword()
        ));

        UserDetails user = (UserDetails) authentication.getPrincipal();
        String token = jwtService.generateToken(user);

        return new AuthResponseDTO(token);
    }
}

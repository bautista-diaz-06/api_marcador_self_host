package com.proyecto.marcador_mapa.services.auth;

import com.proyecto.marcador_mapa.dto.request.LoginRequestDTO;
import com.proyecto.marcador_mapa.dto.request.RefreshTokenRequestDTO;
import com.proyecto.marcador_mapa.dto.request.RegisterRequestDTO;
import com.proyecto.marcador_mapa.dto.response.AuthResponseDTO;
import com.proyecto.marcador_mapa.dto.response.UserResponseDTO;
import com.proyecto.marcador_mapa.entities.Users;
import com.proyecto.marcador_mapa.mapper.users.UserMapper;
import com.proyecto.marcador_mapa.repository.userRepository.UserRepository;
import com.proyecto.marcador_mapa.security.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Slf4j
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
    @Transactional
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
        log.info("Usuario registrado con id={} y email={}", userSaved.getId(), userSaved.getEmail());
        UserResponseDTO response = userMapper.toResponseDTO(userSaved);
        return response;
    }

    @Override
    @Transactional
    public AuthResponseDTO login(LoginRequestDTO loginData) {
        log.info("Intento de login para email={}", loginData.getEmail());

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginData.getEmail(),
                loginData.getPassword()
        ));

        UserDetails user = (UserDetails) authentication.getPrincipal();
        // Se genera el par de tokens en cada login exitoso.
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        log.info("Login exitoso para usuario={}", user.getUsername());

        return new AuthResponseDTO(accessToken, refreshToken);
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponseDTO refreshToken(RefreshTokenRequestDTO refreshData) {
        // Primero validamos que el token recibido sea realmente de tipo refresh.
        if (!jwtService.isRefreshToken(refreshData.getRefreshToken())) {
            log.warn("Refresh token inválido: tipo de token incorrecto");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token inválido");
        }

        String email = jwtService.getEmailFromToken(refreshData.getRefreshToken());
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token inválido"));

        // Se valida firma, expiración, tipo refresh y relación token-usuario.
        if (!jwtService.isRefreshTokenValid(refreshData.getRefreshToken(), user)) {
            log.warn("Refresh token inválido para email={}", email);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token inválido");
        }

        String newAccessToken = jwtService.generateAccessToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);
        log.info("Refresh token exitoso para usuario={}", user.getEmail());

        return new AuthResponseDTO(newAccessToken, newRefreshToken);
    }
}

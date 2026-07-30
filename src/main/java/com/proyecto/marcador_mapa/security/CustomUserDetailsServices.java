package com.proyecto.marcador_mapa.security;

import com.proyecto.marcador_mapa.entities.Users;
import com.proyecto.marcador_mapa.repository.userRepository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsServices implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsServices(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //Este UserDetails es el que permite que luego el proovedor busque una coincidencia a partir de estos details que le pasamos
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("Valor recibido en loadUserByUsername: " + email);

        //El username lo tomamos al 'email', dice username porque es el metodo de la implementacion pero no es obligatorio
        Users foundUser = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        //A pesar de que se espera devolver un UserDetails, nosotros dentro de la entidad Users, ya implementamos
        //la interfaz de UserDetails, entonces como Users ya implementa la interfaz, se puede devolver
        return foundUser;
    }
}

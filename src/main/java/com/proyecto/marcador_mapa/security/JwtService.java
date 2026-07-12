package com.proyecto.marcador_mapa.security;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import io.jsonwebtoken.Claims;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private Long expiration;

    //Genera el token utilizando el UserDetails el cual tomará el 'username' según el campo que nosotros hayamos puesto
    //en el CustomUserDetails
    public String generateToken(UserDetails user){
        return Jwts.builder()
                .subject(user.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), Jwts.SIG.HS256)
                .compact();
    }

    //Obtiene el token y los datos internos que tiene el jwt
    public String getEmailFromToken(String token){
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = getClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    //Obtiene la fecha de expiracion del token
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    //Verifica si el token está expirado o no
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    //Verifica si el token es valido
    public boolean isTokenValid(String token, UserDetails user) {

        //obtiene el 'username' en nuestro caso el email del token
        String username = getEmailFromToken(token);

        //verifica si es username o el campo que configuramos es el mismo que se pasa para verificar si el token es valido
        return username.equals(user.getUsername())
                && !isTokenExpired(token);
    }

    //permite crear el encriptado a nuestra clave secreta
    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}

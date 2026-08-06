package com.proyecto.marcador_mapa.security;


import com.proyecto.marcador_mapa.entities.Users;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import io.jsonwebtoken.Claims;
import java.util.function.Function;

@Service
public class JwtService {

    private static final String TOKEN_TYPE_CLAIM = "token_type";
    private static final String ACCESS_TOKEN_TYPE = "access";
    private static final String REFRESH_TOKEN_TYPE = "refresh";

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private Long expiration;

    @Value("${jwt.refresh-expiration}")
    private Long refreshExpiration;

    //Genera el token utilizando el UserDetails el cual tomará el 'username' según el campo que nosotros hayamos puesto
    //en el CustomUserDetails
    public String generateToken(UserDetails userDetails){
        // Se mantiene por compatibilidad con código existente y devuelve access token.
        return generateAccessToken(userDetails);
    }

    public String generateAccessToken(UserDetails userDetails) {
        // Token corto para autenticar requests en endpoints protegidos.
        Users user = (Users) userDetails;
        return buildToken(user, expiration, ACCESS_TOKEN_TYPE);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        // Token largo para renovar sesión sin volver a pedir credenciales.
        Users user = (Users) userDetails;
        return buildToken(user, refreshExpiration, REFRESH_TOKEN_TYPE);
    }

    private String buildToken(Users user, Long tokenExpiration, String tokenType) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(TOKEN_TYPE_CLAIM, tokenType);

        return Jwts.builder()
                .claims(claims)
                .subject(user.getEmail())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + tokenExpiration))
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
    public boolean isTokenValid(String token, Users user) {

        //obtiene el 'username' en nuestro caso el email del token
        String username = getEmailFromToken(token);

        //verifica si es username o el campo que configuramos es el mismo que se pasa para verificar si el token es valido
        return username.equals(user.getEmail())
                && isAccessToken(token)
                && !isTokenExpired(token);
    }

    public boolean isRefreshToken(String token) {
        return REFRESH_TOKEN_TYPE.equals(extractClaim(token, claims -> claims.get(TOKEN_TYPE_CLAIM, String.class)))
                && !isTokenExpired(token);
    }

    public boolean isRefreshTokenValid(String token, Users user) {
        String username = getEmailFromToken(token);
        return username.equals(user.getEmail())
                && isRefreshToken(token);
    }

    private boolean isAccessToken(String token) {
        return ACCESS_TOKEN_TYPE.equals(extractClaim(token, claims -> claims.get(TOKEN_TYPE_CLAIM, String.class)));
    }

    //permite crear el encriptado a nuestra clave secreta
    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}

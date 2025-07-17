package com.tuempresa.reservas.security;


import com.tuempresa.reservas.model.User;
import com.tuempresa.reservas.repository.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    private static final String SECRET_KEY = "mi_clave_super_secreta_que_deberia_ser_mas_larga";

    public JwtService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }
    private final UserRepository userRepository;

    public String generateToken (String email ){

        User user = userRepository.findByEmail(email)
                .orElseThrow( ()-> new UsernameNotFoundException("usuario no encontrado"));

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole());
        claims.put("id", user.getId());

        return buiildToken(claims, user.getEmail());

    }
    public String buiildToken (Map<String, Object> extraClaims, String subject){
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60*60))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey()) // usa la misma clave para leerlo
                .build()
                .parseClaimsJws(token) // valida el token
                .getBody()
                .getSubject(); // devuelve el "username"
    }
    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            extractUsername(token); // si esto no lanza error, el token es válido
            return true;
        } catch (JwtException e) {
            return false; // token inválido o vencido
        }
    }
}

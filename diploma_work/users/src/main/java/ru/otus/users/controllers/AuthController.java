package ru.otus.users.controllers;

import java.time.Instant;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.users.security.LoginPasswordAuthenticationToken;

@RestController
public class AuthController {
    @Value("${jwt.expiration}")
    Long expirationSeconds;

    @Autowired
    JwtEncoder jwtEncoder;

    @PostMapping("/api/auth/token")
    public String getJwtToken(Authentication authentication) {
        if (!(authentication instanceof LoginPasswordAuthenticationToken)) {
            throw new IllegalStateException("Попытка получить JWT-токен без логина и пароля");
        }

        Instant now = Instant.now();

        String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect( Collectors.joining(" "));

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expirationSeconds))
                .subject(authentication.getName())
                .claim("scope", scope)
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}

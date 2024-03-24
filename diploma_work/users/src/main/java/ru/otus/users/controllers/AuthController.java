package ru.otus.users.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.users.security.LoginPasswordAuthenticationToken;
import ru.otus.users.services.JwtService;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final JwtService jwtService;

    @PostMapping("/api/auth/token")
    public String getJwtToken(Authentication authentication) {
        if (!(authentication instanceof LoginPasswordAuthenticationToken)) {
            throw new IllegalStateException("Попытка получить JWT-токен без логина и пароля");
        }

        return jwtService.getTokenByAuthentication(authentication);
    }
}

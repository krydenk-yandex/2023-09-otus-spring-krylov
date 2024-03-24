package ru.otus.users.services;

import org.springframework.security.core.Authentication;

public interface JwtService {
    String getTokenByAuthentication(Authentication authentication);
}

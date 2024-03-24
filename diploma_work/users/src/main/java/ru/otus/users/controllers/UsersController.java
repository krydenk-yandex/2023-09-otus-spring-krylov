package ru.otus.users.controllers;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.users.dto.UserDto;
import ru.otus.users.exception.UserNotFoundException;
import ru.otus.users.repositories.UserRepository;

@AllArgsConstructor
@RestController
public class UsersController {

    private UserRepository userRepository;

    @GetMapping("/api/users/info")
    public UserDto getInfo(Authentication authentication) throws UserNotFoundException {
        if (!(authentication instanceof JwtAuthenticationToken)) {
            throw new IllegalStateException("Попытка пользователя без JWT-токена");
        }

        var username = authentication.getName();

        return userRepository.findByUsername(username).map(UserDto::new)
                .orElseThrow(UserNotFoundException::new);
    }
}

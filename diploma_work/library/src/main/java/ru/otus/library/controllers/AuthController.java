package ru.otus.library.controllers;

import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.library.clients.UsersClient;
import ru.otus.library.dto.LoginPasswordDto;
import ru.otus.library.dto.UserDto;
import ru.otus.library.exceptions.LoginOrPasswordInvalidException;
import ru.otus.library.security.RemoteAuthentication;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UsersClient usersClient;

    @PostMapping("/api/auth/login")
    public String login(@RequestBody LoginPasswordDto dto) {
        return usersClient.login(dto).orElseThrow(LoginOrPasswordInvalidException::new);
    }

    @RolesAllowed("ROLE_USER")
    @GetMapping("/api/auth")
    public UserDto getAuth(Authentication authentication) {
        return ((RemoteAuthentication)authentication).getPrincipal();
    }

    @ExceptionHandler(LoginOrPasswordInvalidException.class)
    public ResponseEntity<String> handleNotFound(LoginOrPasswordInvalidException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}

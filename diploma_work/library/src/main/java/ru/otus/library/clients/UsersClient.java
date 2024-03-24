package ru.otus.library.clients;

import java.util.Optional;

import ru.otus.library.dto.LoginPasswordDto;
import ru.otus.library.dto.UserDto;

public interface UsersClient {
    Optional<UserDto> getUser(String token);

    Optional<String> login(LoginPasswordDto dto);
}

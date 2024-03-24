package ru.otus.library.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class LoginPasswordDto {
    @NotNull
    private String username;

    @NotNull
    private String password;
}

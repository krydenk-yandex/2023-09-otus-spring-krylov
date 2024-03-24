package ru.otus.users.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.otus.users.models.Authority;
import ru.otus.users.models.User;

@AllArgsConstructor
@Getter
@Setter
public class UserDto {

    private Long id;

    private String username;

    private List<String> authorities;

    public UserDto (User user) {
        id = user.getId();
        username = user.getUsername();
        authorities = user.getAuthorities().stream().map(Authority::getAuthority).toList();
    }
}

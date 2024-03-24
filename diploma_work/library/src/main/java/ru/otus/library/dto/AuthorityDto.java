package ru.otus.library.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

@AllArgsConstructor
@Getter
@Setter
public class AuthorityDto implements GrantedAuthority {
    private String role;

    @Override
    public String getAuthority() {
        return role;
    }
}

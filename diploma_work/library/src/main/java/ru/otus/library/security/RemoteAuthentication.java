package ru.otus.library.security;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import ru.otus.library.dto.AuthorityDto;
import ru.otus.library.dto.UserDto;

public class RemoteAuthentication implements Authentication {

    private final String token;

    private final UserDto user;

    private boolean authenticated;

    public RemoteAuthentication(String token, UserDto user, boolean authenticated) {
        this.token = token;
        this.user = user;
        this.authenticated = authenticated;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getAuthorities().stream().map(AuthorityDto::new).toList();
    }

    @Override
    public String getCredentials() {
        return token;
    }

    @Override
    public UserDto getDetails() {
        return user;
    }

    @Override
    public UserDto getPrincipal() {
        return user;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        authenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return user.getUsername();
    }
}

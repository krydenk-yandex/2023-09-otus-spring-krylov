package ru.otus.users.security;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import ru.otus.users.dto.LoginPasswordDto;
import ru.otus.users.models.User;

public class LoginPasswordAuthenticationToken implements Authentication {

    private LoginPasswordDto credentials;

    private User user;

    private boolean authenticated;

    public LoginPasswordAuthenticationToken(String username, String password, User user, boolean authenticated) {
        this.credentials = new LoginPasswordDto(username, password);
        this.user = user;
        this.authenticated = authenticated;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getAuthorities();
    }

    @Override
    public LoginPasswordDto getCredentials() {
        return credentials;
    }

    @Override
    public User getDetails() {
        return user;
    }

    @Override
    public User getPrincipal() {
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

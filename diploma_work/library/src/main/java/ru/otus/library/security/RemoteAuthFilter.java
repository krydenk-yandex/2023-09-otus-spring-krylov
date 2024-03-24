package ru.otus.library.security;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;
import ru.otus.library.clients.UsersClient;

import static ru.otus.library.utils.AuthUtils.getJwtToken;

@AllArgsConstructor
class RemoteAuthFilter extends GenericFilterBean {

    private UsersClient usersClient;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        var token = getJwtToken();

        token.ifPresent(jwt -> usersClient.getUser(jwt)
                .ifPresent(userDto ->
                        SecurityContextHolder.getContext().setAuthentication(
                                new RemoteAuthentication(
                                        jwt,
                                        userDto,
                                        true
                                )
                        )));

        filterChain.doFilter(servletRequest, servletResponse);
    }

}
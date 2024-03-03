package ru.otus.users.security;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StreamUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.otus.users.repositories.UserRepository;

@AllArgsConstructor
public class LoginPasswordAuthenticationFilter extends OncePerRequestFilter {

    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        byte[] inputStreamBytes = StreamUtils.copyToByteArray(request.getInputStream());

        Map<String, String> jsonRequest = new ObjectMapper().readValue(inputStreamBytes, Map.class);
        String username = jsonRequest.get("username");
        String password = jsonRequest.get("password");

        var user = userRepository.findByUsername(username);

        if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
            SecurityContextHolder.getContext().setAuthentication(
                    new LoginPasswordAuthenticationToken(
                            username,
                            password,
                            user.get(),
                            true
                    ));
        }

        filterChain.doFilter(request, response);
    }
}

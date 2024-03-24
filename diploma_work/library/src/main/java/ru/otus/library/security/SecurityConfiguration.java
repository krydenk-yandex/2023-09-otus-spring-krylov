package ru.otus.library.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import ru.otus.library.clients.UsersClient;

@EnableWebSecurity
@EnableMethodSecurity(jsr250Enabled = true)
@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {

    @Autowired
    private UsersClient usersClient;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(
                authorize ->
                        authorize
                                .requestMatchers("/api/auth/login", "/error").permitAll()
                                .anyRequest().authenticated())
            .httpBasic(AbstractHttpConfigurer::disable)
            .addFilterBefore(
                    new RemoteAuthFilter(usersClient),
                    AnonymousAuthenticationFilter.class
            );

        return http.build();
    }
}

package ru.otus.users.security;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import ru.otus.users.repositories.UserRepository;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfiguration {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Value("${jwt.keys.public}")
    RSAPublicKey publicKey;

    @Value("${jwt.keys.private}")
    RSAPrivateKey privateKey;

    @Bean
    public SecurityFilterChain securityFilterChain( HttpSecurity http ) throws Exception {
        return http
                .authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated())
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(
                        new LoginPasswordAuthenticationFilter(userRepository, passwordEncoder()),
                        AnonymousAuthenticationFilter.class
                )
                .oauth2ResourceServer( config -> config.jwt(withDefaults()) )
                .sessionManagement( ( session ) -> session.sessionCreationPolicy( SessionCreationPolicy.STATELESS ) )
                .exceptionHandling( ( exceptions ) -> exceptions
                        .authenticationEntryPoint( new BearerTokenAuthenticationEntryPoint() )
                        .accessDeniedHandler( new BearerTokenAccessDeniedHandler() )
                ).build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey( this.publicKey).build();
    }

    @Bean
    JwtEncoder jwtEncoder() {
        JWK jwk = new RSAKey
                .Builder(this.publicKey)
                .privateKey(this.privateKey)
                .build();
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>( new JWKSet( jwk ) );
        return new NimbusJwtEncoder( jwks );
    }
}

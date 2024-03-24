package ru.otus.users.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.users.models.Authority;
import ru.otus.users.models.User;
import ru.otus.users.repositories.UserRepository;
import ru.otus.users.security.SecurityConfiguration;
import ru.otus.users.security.UserService;
import ru.otus.users.services.JwtServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({SecurityConfiguration.class, JwtServiceImpl.class})
@WebMvcTest(AuthController.class)
@DisplayName("Контроллер авторизации ")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtEncoder jwtEncoder;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserService userService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    private static final String DUMMY_TOKEN = "dummyToken";

    @BeforeEach
    void setUp() {
        Jwt jwt = Jwt.withTokenValue(DUMMY_TOKEN)
                .header("alg", "none")
                .claim("sub", "user")
                .build();

        when(jwtEncoder.encode(any(JwtEncoderParameters.class))).thenReturn(jwt);
    }

    @DisplayName("должен вернуть корректный JWT-токен")
    @Test
    void getJwtTokenSuccess() throws Exception {
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(new User(
                1L,
                "username",
                "pass",
                true,
                List.of(new Authority(1L, 1L, "USER_ROLE"))
        )));

        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        mockMvc.perform(post("/api/auth/token")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"user\", \"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string(DUMMY_TOKEN));
    }

    @DisplayName("должен вернуть ошибку, если пользователь не найден по логину")
    @Test
    void getJwtTokenFail_noUserFound() throws Exception {
        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/auth/token")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"user\", \"password\":\"password\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string(""));
    }

    @DisplayName("должен вернуть ошибку, если пароль некорректен")
    @Test
    void getJwtTokenFail_incorrectPassword() throws Exception {
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(new User(
                1L,
                "username",
                "pass",
                true,
                List.of(new Authority(1L, 1L, "USER_ROLE"))
        )));

        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        mockMvc.perform(post("/api/auth/token")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"user\", \"password\":\"password\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string(""));
    }
}
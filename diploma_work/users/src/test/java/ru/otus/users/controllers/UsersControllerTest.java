package ru.otus.users.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.users.dto.UserDto;
import ru.otus.users.models.Authority;
import ru.otus.users.models.User;
import ru.otus.users.repositories.UserRepository;
import ru.otus.users.security.SecurityConfiguration;
import ru.otus.users.security.UserService;
import ru.otus.users.services.JwtService;
import ru.otus.users.services.JwtServiceImpl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({SecurityConfiguration.class, JwtServiceImpl.class})
@WebMvcTest(UsersController.class)
@DisplayName("Контроллер данных пользователя ")
class UsersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private JwtService jwtService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserService userService;

    @Test
    @DisplayName("должен вернуть корректный ответ, если токен валидный")
    public void shouldReturnCorrectUserInfo() throws Exception {
        var jwtToken = jwtService.getTokenByAuthentication(
                new TestingAuthenticationToken("", "", Collections.emptyList()));

        var expectedUser = new User(
                1L,
                "username",
                "pass",
                true,
                List.of(new Authority(1L, 1L, "USER_ROLE"))
        );

        when(userRepository.findByUsername(any())).thenReturn(Optional.of(expectedUser));

        var expectedResponse = new UserDto(expectedUser);

        mockMvc.perform(
                get("/api/users/info")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expectedResponse)));
    }

    @Test
    @DisplayName("должен выбросить ошибку, если токен невалидный")
    public void shouldFailWhenTokenIsNotValid() throws Exception {
        var jwtToken = jwtService.getTokenByAuthentication(
                new TestingAuthenticationToken("", "", Collections.emptyList()));
        var incorrectJwtToken = jwtToken + "123~~~!!!";

        mockMvc.perform(
                get("/api/users/info")
                        .header(
                                "Authorization",
                                "Bearer " + incorrectJwtToken))
                .andExpect(status().isUnauthorized());
    }
}
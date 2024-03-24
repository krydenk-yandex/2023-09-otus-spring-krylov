package ru.otus.library.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.library.clients.UsersClient;
import ru.otus.library.controllers.AuthController;
import ru.otus.library.dto.LoginPasswordDto;
import ru.otus.library.services.BookService;
import ru.otus.library.services.FileService;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({SecurityConfiguration.class, FileService.class})
@WebMvcTest(AuthController.class)
@DisplayName("Доступ ")
class AuthControllerAccessTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UsersClient usersClient;

    @MockBean
    private BookService bookService;

    @MockBean
    private FileService fileService;

    @Test
    @DisplayName("к ручке для аутентификации должен быть предоставлен всем")
    void authenticationIsAccessibleForAnonymous() throws Exception {
        String validJwtToken = "someValidToken";

        Mockito.when(usersClient.login(any())).thenReturn(Optional.of(validJwtToken));

        mockMvc.perform(
                post("/api/auth/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(
                                new LoginPasswordDto(
                                        "user",
                                        "pass"
                                )
                        ))
        ).andExpect(status().isOk());
    }
}

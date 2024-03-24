package ru.otus.library.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.library.clients.UsersClient;
import ru.otus.library.controllers.BookController;
import ru.otus.library.dto.UserDto;
import ru.otus.library.services.BookService;
import ru.otus.library.services.FileService;

import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({SecurityConfiguration.class, FileService.class})
@WebMvcTest(BookController.class)
@DisplayName("Доступ ")
class SecurityConfigurationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsersClient usersClient;

    @MockBean
    private BookService bookService;

    @MockBean
    private FileService fileService;

    private final UserDto user = new UserDto(
            1L,
            "user",
            List.of("ROLE_USER")
    );

    private final UserDto admin = new UserDto(
            2L,
            "admin",
            List.of("ROLE_USER", "ROLE_ADMIN")
    );

    @Test
    @DisplayName("к ручке для аутентифицированных должен быть предоставлен пользователю с ролью USER")
    void userAuthorizedForNonAdminArea() throws Exception {
        String validJwtToken = "someValidToken";

        Mockito.when(usersClient.getUser(validJwtToken)).thenReturn(Optional.of(user));

        mockMvc.perform(
                get("/api/books")
                        .header("Authorization", "Bearer " + validJwtToken)
                ).andExpect(status().isOk());
    }

    @Test
    @DisplayName("к ручке для админов должен быть отклонен для пользователя с ролью USER")
    void userNotAuthorizedForAdminArea() throws Exception {
        String validJwtToken = "someValidToken";

        Mockito.when(usersClient.getUser(validJwtToken)).thenReturn(Optional.of(user));

        mockMvc.perform(
                delete("/api/books/1")
                        .header("Authorization", "Bearer " + validJwtToken)
                ).andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("к ручке для админов должен быть предоставлен пользователю с ролью ADMIN")
    void adminAuthorizedForAdminArea() throws Exception {
        String validJwtToken = "someValidToken";

        Mockito.when(usersClient.getUser(validJwtToken)).thenReturn(Optional.of(admin));

        mockMvc.perform(
                delete("/api/books/1")
                        .header("Authorization", "Bearer " + validJwtToken)
                ).andExpect(status().isOk());
    }

    @Test
    @DisplayName("к ручке для аутентифицированных должен быть предоставлен пользователю с ролью ADMIN")
    void adminAuthorizedForUserArea() throws Exception {
        String validJwtToken = "someValidToken";

        Mockito.when(usersClient.getUser(validJwtToken)).thenReturn(Optional.of(admin));

        mockMvc.perform(
                get("/api/books")
                        .header("Authorization", "Bearer " + validJwtToken)
        ).andExpect(status().isOk());
    }

    @Test
    @DisplayName("к ручке для аутентифицированных должен быть отклонен для с невалидным JWT-токеном")
    void userWithInvalidTokenNotAuthorizedForUserArea() throws Exception {
        String validJwtToken = "someInValidToken";

        Mockito.when(usersClient.getUser(validJwtToken)).thenReturn(Optional.empty());

        mockMvc.perform(
                get("/api/books")
                        .header("Authorization", "Bearer " + validJwtToken)
        ).andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("к ручке для аутентифицированных должен быть отклонен для пользователя без JWT-токена")
    void userWithNoTokenNotAuthorizedForUserArea() throws Exception {
        mockMvc.perform(
                get("/api/books")
        ).andExpect(status().isForbidden());
    }
}

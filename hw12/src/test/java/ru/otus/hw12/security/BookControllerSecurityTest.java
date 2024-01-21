package ru.otus.hw12.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw12.controllers.BookController;
import ru.otus.hw12.converters.BookConverter;
import ru.otus.hw12.services.AuthorService;
import ru.otus.hw12.services.BookService;
import ru.otus.hw12.services.GenreService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
@Import(SecurityConfiguration.class)
@DisplayName("Доступ к контроллеру книг ")
public class BookControllerSecurityTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookService bookService;
    @MockBean
    private GenreService genreService;
    @MockBean
    private AuthorService authorService;
    @MockBean
    private BookConverter bookConverter;

    @Test
    @DisplayName(" должен быть у аутентифицированных пользователей по всем страницам")
    @WithMockUser(
            username = "admin",
            authorities = {"ADMIN"}
    )
    public void authenticatedAccess() throws Exception {
        mvc.perform(get("/books")).andExpect(status().isOk());
        mvc.perform(get("/books/edit/123")).andExpect(status().isBadRequest());
        mvc.perform(post("/books/edit/123")).andExpect(status().isBadRequest());
        mvc.perform(get("/books/create")).andExpect(status().isOk());
        mvc.perform(post("/books/create")).andExpect(status().isOk());
        mvc.perform(post("/books/delete/123")).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books"));
    }

    @Test
    @DisplayName("должен быть закрыт по всем страницам для неаутентифицированных пользователей")
    public void notAuthenticatedAccess() throws Exception {
        mvc.perform(get("/books")).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
        mvc.perform(get("/books/edit/123")).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
        mvc.perform(post("/books/edit/123")).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
        mvc.perform(get("/books/create")).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
        mvc.perform(post("/books/create")).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
        mvc.perform(post("/books/delete/123")).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }
}

package ru.otus.hw16.controllers;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw16.models.Genre;
import ru.otus.hw16.services.GenreService;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GenresController.class)
@DisplayName("MVC контроллер для работы с жанрами ")
public class GenreControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private GenreService genreService;

    private final List<Genre> genres = List.of(new Genre(1, "Genre_1"));

    @DisplayName(" должен вернуть список жанров")
    @Test
    public void shouldReturnBooksList() throws Exception {
        given(genreService.findAll()).willReturn(genres);

        mvc.perform(get("/api/genres"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(genres)));
    }
}

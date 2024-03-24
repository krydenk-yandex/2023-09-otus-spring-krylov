package ru.otus.library.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import ru.otus.library.dto.BookReducedDto;
import ru.otus.library.dto.ChapterDto;
import ru.otus.library.dto.ChapterReducedDto;
import ru.otus.library.exceptions.EntityNotFoundException;
import ru.otus.library.services.ChaptersService;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ChaptersController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("Контроллер глав должен ")
class ChaptersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChaptersService chaptersService;

    private final UUID testUUID = UUID.randomUUID();

    @BeforeEach
    public void setup() {
        var book = new BookReducedDto(1L, "Book_1");

        ChapterDto chapterDto = new ChapterDto(testUUID,
                "Chapter Title",
                "Content",
                book,
                null,
                null
        );
        ChapterReducedDto reducedDto = new ChapterReducedDto(testUUID, "Chapter Title");

        given(chaptersService.findByUuid(testUUID)).willReturn(java.util.Optional.of(chapterDto));
        given(chaptersService.findAllByByBookId(1L)).willReturn(List.of(reducedDto));
    }

    @Test
    @DisplayName("возвращать главу, если она найдена по UUID")
    public void getChapterSuccess() throws Exception {
        mockMvc.perform(get("/api/chapters/" + testUUID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uuid").value(testUUID.toString()))
                .andExpect(jsonPath("$.title").value("Chapter Title"));
    }

    @Test
    @DisplayName("возвращать ошибку, если по UUID не найдена глава")
    public void getChapterNotFound() throws Exception {
        given(chaptersService.findByUuid(any(UUID.class))).willThrow(new EntityNotFoundException("The chapter was not found"));

        mockMvc.perform(get("/api/chapters/" + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("The chapter was not found"));
    }

    @Test
    @DisplayName("возвращать главы по книге, если книга существует")
    public void getChaptersListSuccess() throws Exception {
        mockMvc.perform(get("/api/chapters/by-book/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].uuid").value(testUUID.toString()))
                .andExpect(jsonPath("$[0].title").value("Chapter Title"));
    }

    @Test
    @DisplayName("возвращать пустой список глав, если запрошенная книга не существует")
    public void chaptersListEmptyListWhenBookNotExistOrEmpty() throws Exception {
        given(chaptersService.findAllByByBookId(any(Long.class))).willReturn(List.of());

        mockMvc.perform(get("/api/chapters/by-book/99")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }
}
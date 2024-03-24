package ru.otus.library.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.library.models.Bookmark;
import ru.otus.library.models.Chapter;
import ru.otus.library.services.BookmarkService;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookmarkController.class)
@DisplayName("MVC контроллер для работы с авторами ")
@AutoConfigureMockMvc(addFilters = false)
public class BookmarkControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookmarkService bookmarkService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllManualByUserId() throws Exception {
        Bookmark bookmark = new Bookmark(
                1L,
                Bookmark.BookmarkType.MANUAL,
                null,
                1L
        );
        given(bookmarkService.getAllManualByUserId(anyLong())).willReturn(Collections.singletonList(bookmark));

        mockMvc.perform(get("/api/bookmarks/manual/user/{userId}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0]").isNotEmpty());
    }

    @Test
    void getAllManualByBookIdAndUserId() throws Exception {
        given(bookmarkService.getAllManualByBookIdAndUserId(anyLong(), anyLong()))
                .willReturn(Arrays.asList(
                        new Bookmark(
                                1L,
                                Bookmark.BookmarkType.MANUAL,
                                null,
                                1L
                        ),
                        new Bookmark(
                                2L,
                                Bookmark.BookmarkType.MANUAL,
                                null,
                                2L
                        )
                ));

        mockMvc.perform(get("/api/bookmarks/manual/book/{bookId}/user/{userId}", 1L, 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", org.hamcrest.Matchers.hasSize(2)));
    }

    @Test
    void findLatestAutoBookmarkChapterByBookId() throws Exception {
        Chapter chapter = new Chapter();
        given(bookmarkService.findLatestAutoBookmarkChapterByBookId(anyLong(), anyLong()))
                .willReturn(Optional.of(chapter));

        mockMvc.perform(get("/api/bookmarks/auto/latest/book/{bookId}/user/{userId}", 1L, 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isNotEmpty());
    }
}

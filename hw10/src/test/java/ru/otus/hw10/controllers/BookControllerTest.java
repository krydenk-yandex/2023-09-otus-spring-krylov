package ru.otus.hw10.controllers;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw10.dto.AuthorDto;
import ru.otus.hw10.dto.BookDto;
import ru.otus.hw10.dto.GenreDto;
import ru.otus.hw10.models.Author;
import ru.otus.hw10.models.Book;
import ru.otus.hw10.models.Genre;
import ru.otus.hw10.services.BookService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
@DisplayName("MVC контроллер для работы с книгами ")
public class BookControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private BookService bookService;

    private final List<Author> authors = List.of(new Author(1, "Author_1"));
    private final List<Genre> genres = List.of(new Genre(1, "Genre_1"));
    private final List<Book> books = List.of(
            new Book(
                    1,
                    "Book_1",
                    authors.get(0),
                    genres
            )
    );

    private final List<BookDto> booksDtos = books.stream().map(b -> new BookDto(
            b.getId(),
            b.getTitle(),
            new AuthorDto(b.getAuthor().getId(), b.getAuthor().getFullName()),
            b.getGenres().stream().map(g -> new GenreDto(g.getId(), g.getName())).toList()
    )).toList();

    @DisplayName(" должен вернуть список книг")
    @Test
    public void shouldReturnBooksList() throws Exception {
        given(bookService.findAll()).willReturn(booksDtos);

        mvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(booksDtos)));

    }

    @DisplayName(" должен вернуть ошибку 404 при открытии страницы, если книга не найдена")
    @Test
    public void shouldReturn404WhenBookNotFoundOnOpeningEditPage() throws Exception {
        final var BOOK_ID = 1L;

        given(bookService.findById(BOOK_ID)).willReturn(Optional.empty());

        mvc.perform(get("/books/{bookId}", BOOK_ID))
                .andExpect(status().isBadRequest());
    }

    @DisplayName(" должен вернуть сохранить обновленную книгу")
    @Test
    void shouldSaveExistedBook() throws Exception {
        final var BOOK_ID = 1L;
        var expectedBook = books.get(0);

        given(bookService.update(anyLong(), anyString(), anyLong(), any())).willReturn(expectedBook);
        given(bookService.findById(BOOK_ID)).willReturn(Optional.of(expectedBook));

        mvc.perform(post("/books/{bookId}", 1L)
                .param("title", "A new book name")
                .param("authorId", "1")
                .param("genresIds", "1")
        )
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expectedBook)));
    }

    @DisplayName(" должен вернуть ошибку 404, если книга не найдена при попытке сохранения")
    @Test
    void shouldReturn404WhenBookNotFoundOnAttemptToSave() throws Exception {
        given(bookService.findById(anyLong())).willReturn(Optional.empty());

        mvc.perform(post("/books/{bookId}", 1L))
                .andExpect(status().isBadRequest());
    }

    @DisplayName(" должен сохранить новую книгу")
    @Test
    void shouldSaveNewBook() throws Exception {
        var expectedBook = books.get(0);

        given(bookService.insert(anyString(), anyLong(), any())).willReturn(expectedBook);

        mvc.perform(put("/books")
                .param("title", "A new book")
                .param("authorId", "1")
                .param("genresIds", "1")
        )
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expectedBook)));
    }

    @DisplayName(" должен удалить книгу, если она существует")
    @Test
    void shouldDeleteBookWhenBookFound() throws Exception {
        given(bookService.findById(anyLong())).willReturn(Optional.of(books.get(0)));

        mvc.perform(delete("/books/1"))
                .andExpect(status().isOk());
    }

    @DisplayName(" должен вернуть корректный ответ при удалении несуществующей книги")
    @Test
    void shouldDeleteBookWhenBookNotFound() throws Exception {
        given(bookService.findById(anyLong())).willReturn(Optional.empty());

        mvc.perform(delete("/books/1"))
                .andExpect(status().isOk());
    }
}

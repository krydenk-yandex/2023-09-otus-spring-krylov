package ru.otus.hw9.controllers;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw9.converters.BookConverter;
import ru.otus.hw9.dto.AuthorDto;
import ru.otus.hw9.dto.BookDto;
import ru.otus.hw9.dto.BookSaveDto;
import ru.otus.hw9.dto.GenreDto;
import ru.otus.hw9.models.Author;
import ru.otus.hw9.models.Book;
import ru.otus.hw9.models.Genre;
import ru.otus.hw9.services.AuthorService;
import ru.otus.hw9.services.BookService;
import ru.otus.hw9.services.GenreService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
@DisplayName("MVC контроллер для работы с книгами ")
public class BookControllerTest {
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

    private List<Author> authors = List.of(new Author(1, "Author_1"));
    private List<Genre> genres = List.of(new Genre(1, "Genre_1"));
    private List<Book> books = List.of(
            new Book(
                    1,
                    "Book_1",
                    authors.get(0),
                    genres
            )
    );

    private List<BookDto> booksDtos = books.stream().map(b -> new BookDto(
            b.getId(),
            b.getTitle(),
            new AuthorDto(b.getAuthor().getId(), b.getAuthor().getFullName()),
            b.getGenres().stream().map(g -> new GenreDto(g.getId(), g.getName())).toList()
    )).toList();

    List<BookSaveDto> bookSaveDtos = books.stream().map(b -> new BookSaveDto(
            b.getTitle(),
            b.getGenres().stream().map(Genre::getId).toList(),
            b.getAuthor().getId()
    )).toList();

    @DisplayName(" должен вернуть список книг")
    @Test
    public void shouldReturnBooksList() throws Exception {
        given(bookService.findAll()).willReturn(booksDtos);

        mvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("books", booksDtos));
    }

    @DisplayName(" должен вернуть страницу редактирования книги")
    @Test
    public void shouldReturnBookEditPage() throws Exception {
        final var BOOK_ID = 1L;

        given(bookService.findById(BOOK_ID)).willReturn(Optional.of(books.get(0)));
        given(bookConverter.toSaveDto(any())).willReturn(bookSaveDtos.get(0));
        given(genreService.findAll()).willReturn(genres);
        given(authorService.findAll()).willReturn(authors);

        mvc.perform(get("/books/edit/{bookId}", BOOK_ID))
                .andExpect(status().isOk())
                .andExpect(model().attribute("book", bookSaveDtos.get(0)))
                .andExpect(model().attribute("authors", authors))
                .andExpect(model().attribute("genres", genres));
    }

    @DisplayName(" должен вернуть ошибку 404 при открытии страницы, если книга не найдена")
    @Test
    public void shouldReturn404WhenBookNotFoundOnOpeningEditPage() throws Exception {
        final var BOOK_ID = 1L;

        given(bookService.findById(BOOK_ID)).willReturn(Optional.empty());
        given(genreService.findAll()).willReturn(genres);
        given(authorService.findAll()).willReturn(authors);

        mvc.perform(get("/books/edit/{bookId}", BOOK_ID))
                .andExpect(status().isBadRequest());
    }

    @DisplayName(" должен вернуть сохранить обновленную книгу")
    @Test
    void shouldSaveExistedBook() throws Exception {
        final var BOOK_ID = 1L;

        given(bookService.update(anyLong(), anyString(), anyLong(), any())).willReturn(books.get(0));
        given(bookService.findById(BOOK_ID)).willReturn(Optional.of(books.get(0)));

        mvc.perform(post("/books/edit/{bookId}", 1L)
                .param("title", "A new book name")
                .param("authorId", "1")
                .param("genresIds", "1")
        ).andExpect(status().is3xxRedirection());
    }

    @DisplayName(" должен вернуть ошибку 404, если книга не найдена при попытке сохранения")
    @Test
    void shouldReturn404WhenBookNotFoundOnAttemptToSave() throws Exception {
        given(bookService.findById(anyLong())).willReturn(Optional.empty());

        mvc.perform(post("/books/edit/{bookId}", 1L))
                .andExpect(status().isBadRequest());
    }

    @DisplayName(" должен вернуть страницу создания книги")
    @Test
    public void shouldReturnBookCreatePage() throws Exception {
        given(genreService.findAll()).willReturn(genres);
        given(authorService.findAll()).willReturn(authors);

        mvc.perform(get("/books/create"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("authors", authors))
                .andExpect(model().attribute("genres", genres));
    }

    @DisplayName(" должен сохранить новую книгу")
    @Test
    void shouldSaveNewBook() throws Exception {
        given(bookService.insert(anyString(), anyLong(), any())).willReturn(books.get(0));

        mvc.perform(post("/books/create")
                .param("title", "A new book")
                .param("authorId", "1")
                .param("genresIds", "1")
        ).andExpect(status().is3xxRedirection());
    }

    @DisplayName(" должен удалить книгу, если она существует")
    @Test
    void shouldDeleteBookWhenBookFound() throws Exception {
        given(bookService.findById(anyLong())).willReturn(Optional.of(books.get(0)));

        mvc.perform(post("/books/delete/1"))
                .andExpect(status().is3xxRedirection());
    }

    @DisplayName(" должен вернуть корректный ответ при удалении несуществующей книги")
    @Test
    void shouldDeleteBookWhenBookNotFound() throws Exception {
        given(bookService.findById(anyLong())).willReturn(Optional.empty());

        mvc.perform(post("/books/delete/1"))
                .andExpect(status().is3xxRedirection());
    }
}

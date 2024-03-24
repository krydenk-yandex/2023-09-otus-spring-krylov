package ru.otus.library.controllers;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.library.dto.*;
import ru.otus.library.models.Author;
import ru.otus.library.models.Book;
import ru.otus.library.models.Chapter;
import ru.otus.library.models.Genre;
import ru.otus.library.services.BookService;
import ru.otus.library.services.FileService;

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

@Import(FileService.class)
@WebMvcTest(BookController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("MVC контроллер для работы с книгами ")
public class BookControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private BookService bookService;

    @MockBean
    private FileService fileService;

    private final String someBase64Image =  "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAIAAACQd1Pe" +
            "AAAAAXNSR0IArs4c6QAAAAxJREFUGFdjOHMwDAAEQAHkAu6W/gAAAABJRU5ErkJggg==";

    private final String someImageUrl = "/url";

    private final List<Author> authors = List.of(new Author(1, "Author_1"));
    private final List<Genre> genres = List.of(new Genre(1, "Genre_1"));
    private List<Chapter> chapters;
    private List<Book> books;
    private List<BookDto> booksDtos;
    private List<BookWithChaptersDto> booksWithChaptersDtos;

    @BeforeEach
    public void initBooks() {
        var book = new Book();
        book.setId(1L);
        book.setTitle("Book_1");
        book.setCoverUrl(someImageUrl);
        book.setAuthor(this.authors.get(0));
        book.setGenres(genres);

        this.books = Collections.singletonList(book);

        this.chapters = List.of(
                new Chapter(
                        UUID.randomUUID(),
                        1,
                        "Chapter_1",
                        "Chapter_1_text",
                        book,
                        null,
                        null
                )
        );

        book.setChapters(this.chapters);

        this.booksDtos = books.stream().map(b -> new BookDto(
                b.getId(),
                b.getTitle(),
                b.getCoverUrl(),
                new AuthorDto(b.getAuthor().getId(), b.getAuthor().getFullName()),
                b.getGenres().stream().map(g -> new GenreDto(g.getId(), g.getName())).toList()
        )).toList();

        this.booksWithChaptersDtos = books.stream().map(b -> new BookWithChaptersDto(
                b.getId(),
                b.getTitle(),
                b.getCoverUrl(),
                new AuthorDto(b.getAuthor().getId(), b.getAuthor().getFullName()),
                b.getGenres().stream().map(g -> new GenreDto(g.getId(), g.getName())).toList(),
                b.getChapters().stream().map(c -> new ChapterDto(
                        c.getUuid(),
                        c.getTitle(),
                        c.getText(),
                        new BookReducedDto(b.getId(), b.getTitle()),
                        c.getNextChapter() == null ? null : new ChapterReducedDto(
                                c.getNextChapter().getUuid(),
                                c.getNextChapter().getTitle()
                        ),
                        c.getPrevChapter() == null ? null : new ChapterReducedDto(
                                c.getPrevChapter().getUuid(),
                                c.getPrevChapter().getTitle()
                        )
                )).toList()
        )).toList();
    }

    @DisplayName(" должен вернуть список книг")
    @Test
    public void shouldReturnBooksList() throws Exception {
        given(bookService.findAll()).willReturn(booksDtos);

        mvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(booksDtos)));
    }

    @DisplayName(" должен вернуть ошибку 404 при открытии страницы, если книга не найдена")
    @Test
    public void shouldReturn404WhenBookNotFoundOnOpeningEditPage() throws Exception {
        final var BOOK_ID = 1L;

        given(bookService.findById(BOOK_ID)).willReturn(Optional.empty());

        mvc.perform(get("/api/books/{bookId}", BOOK_ID))
                .andExpect(status().isBadRequest());
    }

    @DisplayName(" должен вернуть сохранить обновленную книгу")
    @Test
    void shouldSaveExistedBook() throws Exception {
        final var BOOK_ID = 1L;
        var expectedBook = books.get(0);

        given(bookService.update(anyLong(), anyString(), anyString(), anyLong(), any(), any()))
                .willReturn(booksWithChaptersDtos.get(0));
        given(bookService.findById(BOOK_ID)).willReturn(Optional.of(booksWithChaptersDtos.get(0)));

        given(fileService.putFileToFileStoreAndReturnUrl(any())).willReturn(someImageUrl);

        mvc.perform(put("/api/books/{bookId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(new BookSaveDto(
                        expectedBook.getTitle(),
                        expectedBook.getGenres().stream().map(Genre::getId).toList(),
                        expectedBook.getAuthor().getId(),
                        chapters.stream().map(c -> new ChapterSaveDto(
                                c.getUuid(),
                                c.getTitle(),
                                c.getText()
                        )).toList(),
                        someBase64Image
                ))))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(booksWithChaptersDtos.get(0))));
    }

    @DisplayName(" должен вернуть ошибку 404, если книга не найдена при попытке сохранения")
    @Test
    void shouldReturn404WhenBookNotFoundOnAttemptToSave() throws Exception {
        given(bookService.findById(anyLong())).willReturn(Optional.empty());

        mvc.perform(put("/api/books/{bookId}", 1L))
                .andExpect(status().isBadRequest());
    }

    @DisplayName(" должен сохранить новую книгу")
    @Test
    void shouldSaveNewBook() throws Exception {
        var expectedBook = books.get(0);

        given(bookService.insert(anyString(), anyString(), anyLong(), any(), any())).willReturn(
                booksWithChaptersDtos.get(0)
        );

        given(fileService.putFileToFileStoreAndReturnUrl(any())).willReturn(someImageUrl);

        mvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(new BookSaveDto(
                        expectedBook.getTitle(),
                        expectedBook.getGenres().stream().map(Genre::getId).toList(),
                        expectedBook.getAuthor().getId(),
                        chapters.stream().map(c -> new ChapterSaveDto(
                                c.getUuid(),
                                c.getTitle(),
                                c.getText()
                        )).toList(),
                        someBase64Image
                ))))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(booksWithChaptersDtos.get(0))));
    }

    @DisplayName(" должен удалить книгу, если она существует")
    @Test
    void shouldDeleteBookWhenBookFound() throws Exception {
        given(bookService.findById(anyLong())).willReturn(Optional.of(booksWithChaptersDtos.get(0)));

        mvc.perform(delete("/api/books/1"))
                .andExpect(status().isOk());
    }

    @DisplayName(" должен вернуть корректный ответ при удалении несуществующей книги")
    @Test
    void shouldDeleteBookWhenBookNotFound() throws Exception {
        given(bookService.findById(anyLong())).willReturn(Optional.empty());

        mvc.perform(delete("/api/books/1"))
                .andExpect(status().isOk());
    }
}

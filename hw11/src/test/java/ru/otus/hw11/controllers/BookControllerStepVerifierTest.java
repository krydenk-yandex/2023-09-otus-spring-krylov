package ru.otus.hw11.controllers;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.otus.hw11.dto.BookWithAuthorIdAndGenresIds;
import ru.otus.hw11.models.Author;
import ru.otus.hw11.models.Book;
import ru.otus.hw11.models.Genre;
import ru.otus.hw11.repositories.AuthorRepository;
import ru.otus.hw11.repositories.BookRepository;
import ru.otus.hw11.repositories.GenreRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyIterable;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@WebFluxTest(controllers = BookController.class)
@DisplayName("MVC контроллер для работы с книгами ")
public class BookControllerStepVerifierTest {
    @MockBean
    BookRepository bookRepository;

    @MockBean
    AuthorRepository authorRepository;

    @MockBean
    GenreRepository genreRepository;

    @Autowired
    private WebTestClient client;

    private static List<Author> getDbAuthors() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Author(id, "Author_" + id))
                .toList();
    }

    private static List<Genre> getDbGenres() {
        return IntStream.range(1, 7).boxed()
                .map(id -> new Genre(id, "Genre_" + id))
                .toList();
    }

    private static List<Book> getDbBooks(List<Author> dbAuthors, List<Genre> dbGenres) {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Book(id,
                        "BookTitle_" + id,
                        dbAuthors.get(id - 1),
                        dbGenres.subList((id - 1) * 2, (id - 1) * 2 + 2)
                ))
                .toList();
    }

    private static List<BookWithAuthorIdAndGenresIds> getBooksWithAuthorIdAndGenresIds() {
        return getDbBooks().stream()
                .map(book -> new BookWithAuthorIdAndGenresIds(
                        book.getId(),
                        book.getTitle(),
                        book.getAuthor().getId(),
                        book.getGenres().stream().map(Genre::getId).toList()
                ))
                .toList();
    }

    private static List<Book> getDbBooks() {
        var dbAuthors = getDbAuthors();
        var dbGenres = getDbGenres();
        return getDbBooks(dbAuthors, dbGenres);
    }

    @DisplayName(" должен вернуть список всех книг")
    @Test
    public void shouldReturnAllBooksList() {
        given(bookRepository.findAllWithAuthorIdAndGenresIds()).willReturn(Flux.fromIterable(getBooksWithAuthorIdAndGenresIds()));
        given(authorRepository.findAll()).willReturn(Flux.fromIterable(getDbAuthors()));
        given(genreRepository.findAll()).willReturn(Flux.fromIterable(getDbGenres()));

        var result = client.get().uri("/api/books")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Book.class)
                .getResponseBody()
                .collectList();

        StepVerifier.create(result)
                .assertNext(books -> assertThat(books).usingRecursiveComparison().ignoringCollectionOrder().isEqualTo(getDbBooks()))
                .verifyComplete();
    }

    @DisplayName(" должен вернуть книгу по id")
    @Test
    public void shouldReturnBookById() {
        final var bookId = 1;

        var expectedBookDto = getBooksWithAuthorIdAndGenresIds().stream().filter(book -> book.getId() == bookId)
                .findFirst().get();
        var expectedBook = getDbBooks().stream().filter(book -> book.getId() == expectedBookDto.getId())
                .findFirst().get();

        given(bookRepository.findByIdWithAuthorIdAndGenresIds(anyLong())).willReturn(Mono.just(expectedBookDto));
        given(authorRepository.findById(anyLong())).willReturn(Mono.just(expectedBook.getAuthor()));
        given(genreRepository.findAllById(anyIterable())).willReturn(Flux.fromIterable(expectedBook.getGenres()));

        var result = client.get().uri(String.format("/api/books/%d", bookId))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Book.class)
                .getResponseBody();

        StepVerifier.create(result)
                .assertNext(book -> assertThat(book).usingRecursiveComparison().isEqualTo(expectedBook))
                .verifyComplete();
    }

    @DisplayName(" должен удалить книгу, если книга существует")
    @Test
    void shouldDeleteBookWhenBookFound() {
        given(bookRepository.existsById(anyLong())).willReturn(Mono.just(true));
        given(bookRepository.deleteById(anyLong())).willReturn(Mono.empty());

        var result = client.delete().uri("/api/books/1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Void.class)
                .getResponseBody();

        StepVerifier.create(result)
                .expectComplete()
                .verifyThenAssertThat()
                .hasNotDroppedErrors();
    }

    @DisplayName(" должен вернуть корректный ответ при удалении книги, если книга не существует")
    @Test
    void shouldDeleteBookWhenBookNotFound() {
        given(bookRepository.existsById(anyLong())).willReturn(Mono.just(false));

        var result = client.delete().uri("/api/books/1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Void.class)
                .getResponseBody();

        StepVerifier.create(result)
                .expectComplete()
                .verifyThenAssertThat()
                .hasNotDroppedErrors();
    }

    @DisplayName(" должен сохранить обновленную книгу")
    @Test
    void shouldSaveExistedBook() {
        final var bookId = 1L;
        final var expectedTitle = "BookTitle_UPDATED";
        final var expectedGenresIds = List.of(4L, 5L);
        final var expectedAuthorId = 2L;
        final var expectedBook = new Book(
                bookId,
                expectedTitle,
                getDbAuthors().stream().filter(author -> expectedAuthorId == author.getId()).findFirst().get(),
                getDbGenres().stream().filter(genre -> expectedGenresIds.contains(genre.getId())).toList()
        );
        final var expectedBookDto = new BookWithAuthorIdAndGenresIds(
                expectedBook.getId(),
                expectedBook.getTitle(),
                expectedBook.getAuthor().getId(),
                expectedBook.getGenres().stream().map(Genre::getId).toList()
        );

        given(bookRepository.insertBookGenre(anyLong(), anyLong())).willReturn(Mono.empty());
        given(bookRepository.deleteBookGenres(anyLong())).willReturn(Mono.empty());
        given(bookRepository.update(anyLong(), anyLong(), anyString())).willReturn(Mono.empty());
        given(bookRepository.findByIdWithAuthorIdAndGenresIds(anyLong())).willReturn(Mono.just(expectedBookDto));
        given(authorRepository.findById(anyLong())).willReturn(Mono.just(expectedBook.getAuthor()));
        given(genreRepository.findAllById(anyIterable())).willReturn(Flux.fromIterable(expectedBook.getGenres()));

        var result = client.put().uri(String.format("/api/books/%d", bookId))
                .accept(MediaType.APPLICATION_JSON)
                .body(
                        BodyInserters.fromFormData("title", expectedTitle)
                                .with("authorId", String.valueOf(expectedAuthorId))
                                .with("genresIds", expectedGenresIds.stream().map(Object::toString)
                                        .collect(Collectors.joining(", "))
                                )
                )
                .exchange()
                .expectStatus().isOk()
                .returnResult(Book.class)
                .getResponseBody();

        StepVerifier.create(result)
                .assertNext(actualBook -> assertThat(actualBook)
                        .isNotNull()
                        .usingRecursiveComparison()
                        .ignoringCollectionOrder()
                        .isEqualTo(expectedBook)
                )
                .verifyComplete();
    }

    @DisplayName(" должен сохранить новую книгу")
    @Test
    void shouldSaveNewBook() {
        final var expectedTitle = "BookTitle_UPDATED";
        final var expectedGenresIds = List.of(4L, 5L);
        final var expectedAuthorId = 2L;
        final var expectedBook = new Book(
                1L,
                expectedTitle,
                getDbAuthors().stream().filter(author -> expectedAuthorId == author.getId()).findFirst().get(),
                getDbGenres().stream().filter(genre -> expectedGenresIds.contains(genre.getId())).toList()
        );
        final var expectedBookDto = new BookWithAuthorIdAndGenresIds(
                expectedBook.getId(),
                expectedBook.getTitle(),
                expectedBook.getAuthor().getId(),
                expectedBook.getGenres().stream().map(Genre::getId).toList()
        );

        given(bookRepository.insertBookGenre(anyLong(), anyLong())).willReturn(Mono.empty());
        given(bookRepository.deleteBookGenres(anyLong())).willReturn(Mono.empty());
        given(bookRepository.insert(anyLong(), anyString())).willReturn(Mono.just(1L));
        given(bookRepository.findByIdWithAuthorIdAndGenresIds(anyLong())).willReturn(Mono.just(expectedBookDto));
        given(authorRepository.findById(anyLong())).willReturn(Mono.just(expectedBook.getAuthor()));
        given(genreRepository.findAllById(anyIterable())).willReturn(Flux.fromIterable(expectedBook.getGenres()));

        var result = client.post().uri("/api/books")
                .accept(MediaType.APPLICATION_JSON)
                .body(
                        BodyInserters.fromFormData("title", expectedTitle)
                                .with("authorId", String.valueOf(expectedAuthorId))
                                .with("genresIds", expectedGenresIds.stream().map(Object::toString)
                                        .collect(Collectors.joining(", "))
                                )
                )
                .exchange()
                .expectStatus().isOk()
                .returnResult(Book.class)
                .getResponseBody();

        StepVerifier.create(result)
                .assertNext(actualBook -> assertThat(actualBook).isNotNull()
                        .usingRecursiveComparison()
                        .ignoringCollectionOrder()
                        .isEqualTo(expectedBook))
                .verifyComplete();
    }
}

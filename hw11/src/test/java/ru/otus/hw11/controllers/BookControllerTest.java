package ru.otus.hw11.controllers;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import ru.otus.hw11.models.Author;
import ru.otus.hw11.models.Book;
import ru.otus.hw11.models.Genre;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("MVC контроллер для работы с книгами ")
public class BookControllerTest {
    @LocalServerPort
    private int port;

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

    private static List<Book> getDbBooks() {
        var dbAuthors = getDbAuthors();
        var dbGenres = getDbGenres();
        return getDbBooks(dbAuthors, dbGenres);
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @DisplayName(" должен вернуть книгу по id")
    @Test
    public void shouldReturnBookById() {
        var client = WebClient.create(String.format("http://localhost:%d", port));
        final var BOOK_ID = 1;

        var result = client.get().uri(String.format("/api/books/%d", BOOK_ID))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Book.class)
                .timeout(Duration.ofSeconds(3))
                .block();

        var expectedBook = getDbBooks().stream().filter(b -> b.getId() == BOOK_ID).findFirst().get();

        assertThat(result).usingRecursiveComparison().ignoringCollectionOrder().isEqualTo(expectedBook);
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @DisplayName(" должен вернуть список всех книг")
    @Test
    public void shouldReturnAllBooksList() {
        var client = WebClient.create(String.format("http://localhost:%d", port));

        var result = client.get().uri("/api/books")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(Book.class)
                .take(3)
                .timeout(Duration.ofSeconds(3))
                .collectList()
                .block();

        assertThat(result).usingRecursiveComparison().ignoringCollectionOrder().isEqualTo(getDbBooks());
    }

    @DisplayName(" должен удалить книгу, если книга существует")
    @Test
    void shouldDeleteBookWhenBookFound() {
        var client = WebClient.create(String.format("http://localhost:%d", port));

        var result = client.delete().uri("/api/books/1")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toBodilessEntity()
                .block();

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode().is2xxSuccessful()).isTrue();
    }

    @DisplayName(" должен вернуть корректный ответ при удалении книги, если книга не существует")
    @Test
    void shouldDeleteBookWhenBookNotFound() {
        var client = WebClient.create(String.format("http://localhost:%d", port));

        var result = client.delete().uri("/api/books/255")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toBodilessEntity()
                .block();

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode().is2xxSuccessful()).isTrue();
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @DisplayName(" должен сохранить обновленную книгу")
    @Test
    void shouldSaveExistedBook() {
        var client = WebClient.create(String.format("http://localhost:%d", port));

        final var BOOK_ID = 1L;
        final var EXPECTED_TITLE = "BookTitle_UPDATED";
        final var EXPECTED_GENRES_IDS = List.of(4L, 5L);
        final var EXPECTED_AUTHOR_ID = 2L;

        Book result = client.put().uri(String.format("/api/books/%d", BOOK_ID))
                .accept(MediaType.APPLICATION_JSON)
                .body(
                        BodyInserters.fromFormData("title", EXPECTED_TITLE)
                                .with("authorId", String.valueOf(EXPECTED_AUTHOR_ID))
                                .with("genresIds", EXPECTED_GENRES_IDS.stream().map(Object::toString)
                                    .collect(Collectors.joining(", "))
                                )
                )
                .retrieve()
                .bodyToMono(Book.class)
                .block();

        assertThat(result)
                .isNotNull()
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .isEqualTo(new Book(
                        BOOK_ID,
                        EXPECTED_TITLE,
                        getDbAuthors().stream().filter(a -> EXPECTED_AUTHOR_ID == a.getId()).findFirst().get(),
                        getDbGenres().stream().filter(g -> EXPECTED_GENRES_IDS.contains(g.getId())).toList()
                ));
    }

    @DisplayName(" должен сохранить новую книгу")
    @Test
    void shouldSaveNewBook() {
        var client = WebClient.create(String.format("http://localhost:%d", port));

        final var EXPECTED_TITLE = "BookTitle_UPDATED";
        final var EXPECTED_GENRES_IDS = List.of(4L, 5L);
        final var EXPECTED_AUTHOR_ID = 2L;

        Book result = client.post().uri("/api/books")
                .accept(MediaType.APPLICATION_JSON)
                .body(
                        BodyInserters.fromFormData("title", EXPECTED_TITLE)
                                .with("authorId", String.valueOf(EXPECTED_AUTHOR_ID))
                                .with("genresIds", EXPECTED_GENRES_IDS.stream().map(Object::toString)
                                        .collect(Collectors.joining(", "))
                                )
                )
                .retrieve()
                .bodyToMono(Book.class)
                .block();
        assertThat(result).isNotNull()
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .ignoringFields("id")
                .isEqualTo(new Book(
                        0L,
                        EXPECTED_TITLE,
                        getDbAuthors().stream().filter(a -> EXPECTED_AUTHOR_ID == a.getId()).findFirst().get(),
                        getDbGenres().stream().filter(g -> EXPECTED_GENRES_IDS.contains(g.getId())).toList()
                ));


        assertThat(result.getId()).isGreaterThan(0);
    }
}

package ru.otus.hw8.repositories;

import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.hw8.models.Author;
import ru.otus.hw8.models.Book;
import ru.otus.hw8.models.Genre;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Mongo Spring Data для работы с книгами ")
@DataMongoTest
class BookRepositoryTest {

    @Autowired
    private BookRepository repository;

    @Autowired
    private MongoOperations operations;


    @DisplayName("должен загружать книгу по id")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void shouldReturnCorrectBookById() {
        final String BOOK_ID = "1";

        var actualBook = repository.findById(BOOK_ID);
        var expectedBook = getDbBooks().stream()
                .filter(b -> Objects.equals(b.getId(), BOOK_ID))
                .findFirst()
                .get();

        assertThat(actualBook).isPresent().get()
                .usingRecursiveComparison()
                .isEqualTo(expectedBook);
    }

    @DisplayName("должен загружать список всех книг")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void shouldReturnCorrectBooksList() {
        var actualBooks = repository.findAll();

        assertThat(actualBooks).isNotEmpty();

        var expectedBooks = getDbBooks();

        assertThat(actualBooks)
            .usingRecursiveFieldByFieldElementComparator()
            .containsExactlyElementsOf(expectedBooks);
    }

    @DisplayName("должен сохранять новую книгу")
    @Test
    void shouldSaveNewBook() {
        var expectedBook = new Book(
                null,
                "Book_10500",
                getDbAuthors().get(0),
                List.of(
                        getDbGenres().get(0),
                        getDbGenres().get(1)
                )
        );

        var returnedBook = repository.save(expectedBook);

        assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId().length() > 0)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expectedBook);

        assertThat(returnedBook)
                .usingRecursiveComparison()
                .isEqualTo(
                        operations.findById(returnedBook.getId(), Book.class)
                );
    }

    @DisplayName("должен сохранять измененную книгу")
    @Test
    void shouldSaveUpdatedBook() {
        final var BOOK_ID = "1";

        var expectedBook = new Book(
                BOOK_ID,
                "Book_10500",
                getDbAuthors().get(0),
                List.of(
                        getDbGenres().get(0),
                        getDbGenres().get(1)
                )
        );

        assertThat(operations.findById(BOOK_ID, Book.class))
                .usingRecursiveComparison()
                .isNotNull()
                .isNotEqualTo(expectedBook);

        var returnedBook = repository.save(expectedBook);

        assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId().length() > 0)
                .usingRecursiveComparison()
                .isEqualTo(expectedBook);

        assertThat(returnedBook)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(operations.findById(returnedBook.getId(), Book.class));
    }

    @DisplayName("должен удалять книгу по id")
    @Test
    void shouldDeleteBook() {
        final var BOOK_ID = "1";

        var bookToDelete = operations.findById(BOOK_ID, Book.class);

        assertThat(bookToDelete).isNotNull();

        repository.deleteById(BOOK_ID);

        assertThat(operations.findById(BOOK_ID, Book.class)).isNull();
    }

    private static List<Author> getDbAuthors() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Author(id.toString(), "Author_" + id))
                .toList();
    }

    private static List<Genre> getDbGenres() {
        return IntStream.range(1, 7).boxed()
                .map(id -> new Genre(id.toString(), "Genre_" + id))
                .toList();
    }

    private static List<Book> getDbBooks(List<Author> dbAuthors, List<Genre> dbGenres) {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Book(id.toString(),
                        "Book_" + id,
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
}
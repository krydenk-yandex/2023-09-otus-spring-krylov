package ru.otus.hw10.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw10.models.Author;
import ru.otus.hw10.models.Book;
import ru.otus.hw10.models.Genre;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Spring Data для работы с книгами ")
@DataJpaTest
class BookRepositoryTest {

    @Autowired
    private BookRepository repository;

    @Autowired
    private TestEntityManager em;

    @DisplayName("должен загружать книгу по id")
    @Test
    void shouldReturnCorrectBookById() {
        var actualBook = repository.findById(1);
        em.detach(actualBook.get());

        var expectedBook = em.find(Book.class, 1);

        assertThat(actualBook).isPresent().get()
                .usingRecursiveComparison()
                .isEqualTo(expectedBook);
    }

    @DisplayName("должен загружать список всех книг")
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
                0,
                "BookTitle_10500",
                em.find(Author.class, 1),
                List.of(
                        em.find(Genre.class, 1),
                        em.find(Genre.class, 2))
                );
        var returnedBook = repository.save(expectedBook);

        assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison()
                .isEqualTo(expectedBook);

        em.flush();
        em.detach(returnedBook);

        assertThat(returnedBook)
                .usingRecursiveComparison()
                .isEqualTo(em.find(Book.class, returnedBook.getId()));
    }

    @DisplayName("должен сохранять измененную книгу")
    @Test
    void shouldSaveUpdatedBook() {
        var expectedBook = new Book(1L,
                "BookTitle_10500",
                em.find(Author.class, 2),
                List.of(em.find(Genre.class, 1),
                        em.find(Genre.class, 2))
        );

        assertThat(em.find(Book.class, expectedBook.getId()))
                .usingRecursiveComparison()
                .isNotNull()
                .isNotEqualTo(expectedBook);

        var returnedBook = repository.save(expectedBook);

        assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison()
                .isEqualTo(expectedBook);

        em.flush();
        em.detach(returnedBook);

        assertThat(returnedBook)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(em.find(Book.class, returnedBook.getId()));
    }

    @DisplayName("должен удалять книгу по id")
    @Test
    void shouldDeleteBook() {
        var bookToDelete = em.find(Book.class, 1L);
        var bookGenresAssociationsQuery = em.getEntityManager().createNativeQuery(
                        "select * from books_genres where book_id = :book_id")
                .setParameter("book_id", bookToDelete.getId());

        assertThat(bookToDelete).isNotNull();
        assertThat(bookGenresAssociationsQuery.getResultList()).isNotEmpty();

        repository.deleteById(1L);
        em.flush();
        em.detach(bookToDelete);

        assertThat(bookGenresAssociationsQuery.getResultList()).isEmpty();
        assertThat(em.find(Book.class, 1L)).isNull();
    }

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
}
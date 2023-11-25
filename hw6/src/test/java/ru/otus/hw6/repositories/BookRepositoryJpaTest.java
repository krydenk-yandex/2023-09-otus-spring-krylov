package ru.otus.hw6.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw6.models.Author;
import ru.otus.hw6.models.Book;
import ru.otus.hw6.models.Genre;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе JPA для работы с книгами ")
@DataJpaTest
@Import({BookRepositoryJpa.class})
class BookRepositoryJpaTest {

    @Autowired
    private BookRepositoryJpa repository;

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
        var expectedBooks = actualBooks.stream()
                .map(book -> {
                    em.detach(book);
                    return em.find(Book.class, book.getId());
                })
                .collect(Collectors.toList());

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

    @DisplayName("должен удалять книгу по id ")
    @Test
    void shouldDeleteBook() {
        var bookToDelete = em.find(Book.class, 1L);
        assertThat(bookToDelete).isNotNull();

        repository.deleteById(1L);

        em.flush();
        em.detach(bookToDelete);

        assertThat(em.find(Book.class, 1L)).isNull();
    }
}
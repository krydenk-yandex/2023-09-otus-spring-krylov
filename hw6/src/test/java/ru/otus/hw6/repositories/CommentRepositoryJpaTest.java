package ru.otus.hw6.repositories;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw6.models.Author;
import ru.otus.hw6.models.Book;
import ru.otus.hw6.models.Comment;
import ru.otus.hw6.models.Genre;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе JPA для работы с комментариями к книге ")
@DataJpaTest
@Import({CommentRepositoryJpa.class, BookRepositoryJpa.class})
class CommentRepositoryJpaTest {

    @Autowired
    private CommentRepositoryJpa repository;

    @Autowired
    private TestEntityManager em;

    @DisplayName("должен загружать комментарий по id")
    @Test
    void shouldReturnCorrectCommentById() {
        var expectedComment = em.getEntityManager().find(Comment.class, 1);
        var actualComment = repository.findById(expectedComment.getId());

        assertThat(actualComment).isPresent();
        assertThat(actualComment.get()).isEqualTo(expectedComment);
    }

    @DisplayName("должен загружать список комментариев по id книги")
    @Test
    void shouldReturnCorrectCommentsListByBookId() {
        var actualComments = repository.findByBookId(1);
        var expectedBook = new Book(
            1,
            "BookTitle_1",
            new Author(1, "Author_1"),
            List.of(
                new Genre(1, "Genre_1"),
                new Genre(2, "Genre_2")
            )
        );

        assertThat(actualComments)
            .usingRecursiveFieldByFieldElementComparator()
            .containsExactlyInAnyOrder(
                new Comment(
                    1,
                    "Comment #1",
                    "Person #1",
                    expectedBook
                ),
                new Comment(
                    2,
                    "Comment #2",
                    "Person #2",
                    expectedBook
                ));
    }
}
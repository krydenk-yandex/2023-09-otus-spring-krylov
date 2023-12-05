package ru.otus.hw6.repositories;

import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw6.models.Book;
import ru.otus.hw6.models.Comment;

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
        var actualComment = repository.findById(1);
        em.detach(actualComment.get());

        var expectedComment = em.find(Comment.class, 1);

        assertThat(actualComment).isPresent();
        assertThat(actualComment.get()).isEqualTo(expectedComment);
    }

    @DisplayName("должен загружать список комментариев по id книги")
    @Test
    void shouldReturnCorrectCommentsListByBookId() {
        var actualComments = repository.findByBookId(1);

        var expectedComments = actualComments.stream()
                .map(comment -> {
                    em.detach(comment);
                    return em.find(Comment.class, comment.getId());
                }).collect(Collectors.toList());

        assertThat(actualComments)
            .usingRecursiveFieldByFieldElementComparator()
            .containsExactlyElementsOf(expectedComments);
    }

    @DisplayName("должен сохранять новый комментарий")
    @Test
    void shouldSaveNewComment() {
        var expectedComment = new Comment(
                0,
                "Comment text",
                "Author name",
                em.find(Book.class, 1)
        );
        var returnedComment = repository.save(expectedComment);

        assertThat(returnedComment).isNotNull()
                .matches(comment -> comment.getId() > 0)
                .usingRecursiveComparison()
                .isEqualTo(expectedComment);

        em.flush();
        em.detach(returnedComment);

        assertThat(returnedComment)
                .usingRecursiveComparison()
                .isEqualTo(em.find(Comment.class, returnedComment.getId()));
    }

    @DisplayName("должен сохранять измененный комментарий")
    @Test
    void shouldSaveUpdatedComment() {
        var expectedComment = new Comment(
                1L,
                "Comment text updated",
                "Comment author name absolutely updated",
                em.find(Book.class, 1)
        );

        assertThat(em.find(Comment.class, expectedComment.getId()))
                .usingRecursiveComparison()
                .isNotNull()
                .isNotEqualTo(expectedComment);

        var returnedComment = repository.save(expectedComment);

        assertThat(returnedComment).isNotNull()
                .matches(comment -> comment.getId() > 0)
                .usingRecursiveComparison()
                .isEqualTo(expectedComment);

        em.flush();
        em.detach(returnedComment);

        assertThat(returnedComment)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(em.find(Comment.class, returnedComment.getId()));
    }

    @DisplayName("должен удалять коммент по id ")
    @Test
    void shouldDeleteBook() {
        var commentToDelete = em.find(Comment.class, 1L);
        assertThat(commentToDelete).isNotNull();

        repository.deleteById(1L);

        em.flush();
        em.detach(commentToDelete);

        assertThat(em.find(Comment.class, 1L)).isNull();
    }
}
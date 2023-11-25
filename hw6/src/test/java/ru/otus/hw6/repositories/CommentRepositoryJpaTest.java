package ru.otus.hw6.repositories;

import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
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
}
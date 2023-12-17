package ru.otus.hw8.repositories;

import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import ru.otus.hw8.models.Book;
import ru.otus.hw8.models.Comment;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Mongo Spring Data для работы с комментариями к книге ")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DataMongoTest
class CommentRepositoryTest {

    @Autowired
    private CommentRepository repository;

    @DisplayName("должен загружать комментарий по id")
    @Test
    @Order(1)
    void shouldReturnCorrectCommentById() {
        final String COMMENT_ID = "1";

        var actualComment = repository.findById(COMMENT_ID);

        assertThat(actualComment).isPresent();
        assertThat(actualComment.get()).usingRecursiveComparison()
                .ignoringFields(
                    "book.author",
                    "book.genres"
                )
                .isEqualTo(getDbComments().stream()
                        .filter(c -> c.getId().equals(COMMENT_ID)).findFirst().get()
        );
    }

    @DisplayName("должен загружать список комментариев по id книги")
    @Test
    @Order(2)
    void shouldReturnCorrectCommentsListByBookId() {
        final String BOOK_ID = "1";

        var actualComments = repository.findByBookId(BOOK_ID);

        assertThat(actualComments)
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields(
                    "book.author",
                    "book.genres"
            )
            .containsExactlyElementsOf(
                    getDbComments().stream().filter(
                            c -> c.getBook().getId().equals(BOOK_ID)).toList());
    }

    @DisplayName("должен сохранять новый комментарий")
    @Test
    void shouldSaveNewComment() {
        var expectedComment = new Comment(
                null,
                "Comment text",
                "Author name",
                getDbBooks().stream().findFirst().get()
        );

        var returnedComment = repository.save(expectedComment);

        assertThat(returnedComment).isNotNull()
                .matches(comment -> comment.getId().length() > 0)
                .usingRecursiveComparison()
                .isEqualTo(expectedComment);


        assertThat(returnedComment)
                .usingRecursiveComparison()
                .ignoringFields(
                        "book.author",
                        "book.genres"
                )
                .isEqualTo(repository.findById(returnedComment.getId()).get());
    }

    @DisplayName("должен сохранять измененный комментарий")
    @Test
    @Order(3)
    void shouldSaveUpdatedComment() {
        var expectedComment = new Comment(
                "1",
                "Comment text updated",
                "Comment author name absolutely updated",
                getDbBooks().stream().findFirst().get()
        );

        assertThat(repository.findById(expectedComment.getId()).get())
                .usingRecursiveComparison()
                .isNotNull()
                .isNotEqualTo(expectedComment);

        var returnedComment = repository.save(expectedComment);

        assertThat(returnedComment).isNotNull()
                .matches(comment -> comment.getId().length() > 0)
                .usingRecursiveComparison()
                .ignoringFields(
                        "book.author",
                        "book.genres"
                )
                .isEqualTo(expectedComment);

        assertThat(returnedComment)
                .isNotNull()
                .usingRecursiveComparison()
                .ignoringFields(
                        "book.author",
                        "book.genres"
                )
                .isEqualTo(repository.findById(expectedComment.getId()).get());
    }

    @DisplayName("должен удалять коммент по id")
    @Test
    void shouldDeleteComment() {
        final var COMMENT_ID = "1";

        var commentToDelete = repository.findById(COMMENT_ID);
        assertThat(commentToDelete).isNotNull();

        repository.deleteById(COMMENT_ID);

        assertThat(repository.findById(COMMENT_ID)).isEmpty();
    }

    private static List<Comment> getDbComments() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Comment(
                        id.toString(),
                        "Comment_%d".formatted(id),
                        "CommentAuthor_%d".formatted(id),
                        getDbBooks().get(id - 1)
                ))
                .toList();
    }

    private static List<Book> getDbBooks() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Book(
                        id.toString(),
                        "Book_" + id,
                        null,
                        null
                ))
                .toList();
    }
}
package ru.otus.hw8.repositories;

import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.hw8.models.Book;
import ru.otus.hw8.models.Comment;
import ru.otus.hw8.projections.IdProjection;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Mongo Spring Data для работы с комментариями к книге ")
@DataMongoTest
class CommentRepositoryTest {

    @Autowired
    private CommentRepository repository;

    @Autowired
    private MongoOperations operations;

    @DisplayName("должен загружать комментарий по id")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
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
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
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

    @DisplayName("должен загружать список id комментариев по id книги")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void shouldReturnCorrectCommentsIdsProjectionsListByBookId() {
        final String BOOK_ID = "1";

        var actualIds = repository.findIdsByBookId(BOOK_ID)
                .stream().map(IdProjection::_id).toList();

        assertThat(actualIds)
            .containsExactlyInAnyOrderElementsOf(
                    getDbComments().stream()
                        .filter(c -> c.getBook().getId().equals(BOOK_ID))
                        .map(Comment::getId)
                        .toList()
            );
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
                .isEqualTo(operations.findById(returnedComment.getId(), Comment.class));
    }

    @DisplayName("должен сохранять измененный комментарий")
    @Test
    void shouldSaveUpdatedComment() {
        var expectedComment = new Comment(
                "1",
                "Comment text updated",
                "Comment author name absolutely updated",
                getDbBooks().stream().findFirst().get()
        );

        assertThat(operations.findById(expectedComment.getId(), Comment.class))
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
                .isEqualTo(operations.findById(returnedComment.getId(), Comment.class));
    }

    @DisplayName("должен удалять коммент по id")
    @Test
    void shouldDeleteComment() {
        final var COMMENT_ID = "1";

        var commentToDelete = operations.findById(COMMENT_ID, Comment.class);
        assertThat(commentToDelete).isNotNull();

        repository.deleteById(COMMENT_ID);

        assertThat(operations.findById(COMMENT_ID, Comment.class)).isNull();
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
package ru.otus.hw6.repositories;

import java.util.List;
import java.util.Optional;

import ru.otus.hw6.models.Comment;

public interface CommentRepository {
    List<Comment> findByBookId(long bookId);

    Optional<Comment> findById(long id);

    Comment save(Comment comment);

    void deleteById(long id);
}

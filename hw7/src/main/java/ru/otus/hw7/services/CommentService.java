package ru.otus.hw7.services;

import java.util.List;
import java.util.Optional;

import ru.otus.hw7.models.Comment;

public interface CommentService {
    Optional<Comment> findById(long id);

    List<Comment> findByBookId(long bookId);

    Comment insert(String text, String authorName, long bookId);

    Comment update(long id, String text, String authorName, long bookId);

    void deleteById(long id);
}

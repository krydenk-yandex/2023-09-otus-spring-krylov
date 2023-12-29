package ru.otus.hw10.services;

import java.util.List;
import java.util.Optional;

import ru.otus.hw10.models.Comment;

public interface CommentService {
    Optional<Comment> findById(long id);

    List<Comment> findByBookId(long bookId);

    Comment insert(String text, String authorName, long bookId);

    Comment update(long id, String text, String authorName, long bookId);

    void deleteById(long id);
}

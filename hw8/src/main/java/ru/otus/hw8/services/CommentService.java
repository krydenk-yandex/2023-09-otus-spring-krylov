package ru.otus.hw8.services;

import java.util.List;
import java.util.Optional;

import ru.otus.hw8.models.Comment;

public interface CommentService {
    Optional<Comment> findById(String id);

    List<Comment> findByBookId(String bookId);

    Comment insert(String text, String authorName, String bookId);

    Comment update(String id, String text, String authorName, String bookId);

    void deleteById(String id);
}

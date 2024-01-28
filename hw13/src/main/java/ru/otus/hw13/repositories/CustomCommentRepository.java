package ru.otus.hw13.repositories;

import java.util.List;

import ru.otus.hw13.models.Comment;

public interface CustomCommentRepository {
    List<Comment> findByBookId(long bookId);
}

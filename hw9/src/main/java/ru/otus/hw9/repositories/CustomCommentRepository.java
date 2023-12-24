package ru.otus.hw9.repositories;

import java.util.List;

import ru.otus.hw9.models.Comment;

public interface CustomCommentRepository {
    List<Comment> findByBookId(long bookId);
}

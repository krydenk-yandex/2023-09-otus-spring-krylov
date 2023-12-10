package ru.otus.hw7.repositories;

import java.util.List;

import ru.otus.hw7.models.Comment;

public interface CustomCommentRepository {
    List<Comment> findByBookId(long bookId);
}

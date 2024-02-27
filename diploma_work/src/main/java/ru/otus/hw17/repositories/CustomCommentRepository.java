package ru.otus.hw17.repositories;

import java.util.List;

import ru.otus.hw17.models.Comment;

public interface CustomCommentRepository {
    List<Comment> findByBookId(long bookId);
}

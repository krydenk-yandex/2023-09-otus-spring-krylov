package ru.otus.hw12.repositories;

import java.util.List;

import ru.otus.hw12.models.Comment;

public interface CustomCommentRepository {
    List<Comment> findByBookId(long bookId);
}

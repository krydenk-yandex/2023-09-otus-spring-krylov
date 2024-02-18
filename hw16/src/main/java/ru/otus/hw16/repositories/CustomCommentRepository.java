package ru.otus.hw16.repositories;

import java.util.List;

import ru.otus.hw16.models.Comment;

public interface CustomCommentRepository {
    List<Comment> findByBookId(long bookId);
}

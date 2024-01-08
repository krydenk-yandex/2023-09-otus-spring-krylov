package ru.otus.hw10.repositories;

import java.util.List;

import ru.otus.hw10.models.Comment;

public interface CustomCommentRepository {
    List<Comment> findByBookId(long bookId);
}

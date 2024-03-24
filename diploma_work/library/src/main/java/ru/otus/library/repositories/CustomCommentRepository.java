package ru.otus.library.repositories;

import java.util.List;

import ru.otus.library.models.Comment;

public interface CustomCommentRepository {
    List<Comment> findByBookId(long bookId);
}

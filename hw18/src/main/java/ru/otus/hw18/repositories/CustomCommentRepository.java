package ru.otus.hw18.repositories;

import java.util.List;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import ru.otus.hw18.models.Comment;

@CircuitBreaker(name = "database")
public interface CustomCommentRepository {
    List<Comment> findByBookId(long bookId);
}

package ru.otus.hw18.repositories;

import java.util.List;
import java.util.Optional;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw18.models.Comment;


@CircuitBreaker(name = "database")
public interface CommentRepository extends CustomCommentRepository, JpaRepository<Comment, Long> {

    @Override
    List<Comment> findByBookId(long bookId);

    @Override
    @EntityGraph(attributePaths = {"book"})
    Optional<Comment> findById(Long aLong);
}

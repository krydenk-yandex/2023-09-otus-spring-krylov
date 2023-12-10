package ru.otus.hw7.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw7.models.Comment;

public interface CommentRepository extends CustomCommentRepository, JpaRepository<Comment, Long> {

    @Override
    List<Comment> findByBookId(long bookId);

    @Override
    @EntityGraph(attributePaths = {"book"})
    Optional<Comment> findById(Long aLong);
}

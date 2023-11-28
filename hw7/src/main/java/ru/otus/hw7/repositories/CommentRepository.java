package ru.otus.hw7.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw7.models.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByBookId(long bookId);
}

package ru.otus.hw8.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw8.models.Comment;

public interface CommentRepository extends MongoRepository<Comment, String> {

    List<Comment> findByBookId(String bookId);

    Optional<Comment> findById(String id);
}

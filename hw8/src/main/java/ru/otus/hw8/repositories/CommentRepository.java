package ru.otus.hw8.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.hw8.projections.IdProjection;
import ru.otus.hw8.models.Comment;

public interface CommentRepository extends MongoRepository<Comment, String> {

    List<Comment> findByBookId(String bookId);

    @Query(value = "{'bookId': :#{#bookId}}", fields = "{'_id': 1}")
    List<IdProjection> findIdsByBookId(@Param("bookId") String bookId);

    Optional<Comment> findById(String id);
}

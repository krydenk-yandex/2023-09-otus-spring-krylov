package ru.otus.hw11.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw11.models.Comment;

public interface CommentRepository extends ReactiveCrudRepository<Comment, Long> {
    Flux<Comment> findByBookId(long bookId);

    Mono<Comment> findById(Long aLong);
}

package ru.otus.hw11.repositories;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw11.dto.AuthorWithBookIdDto;
import ru.otus.hw11.models.Author;

public interface AuthorRepository extends ReactiveCrudRepository<Author, Long> {
    @Query("""
        select a.id, a.full_name
        from authors a
        join books b on b.author_id = a.id
        where b.id = :bookId
    """)
    Mono<Author> findByBookId(@Param("bookId") Long bookId);

    @Query("""
        select a.id, a.full_name, b.id as book_id
        from books b
        join authors a on b.author_id = a.id
    """)
    Flux<AuthorWithBookIdDto> findAllWithBookId();
}

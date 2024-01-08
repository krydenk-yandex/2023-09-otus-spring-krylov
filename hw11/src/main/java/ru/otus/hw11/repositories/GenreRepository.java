package ru.otus.hw11.repositories;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import ru.otus.hw11.dto.GenreWithBookIdDto;
import ru.otus.hw11.models.Genre;

public interface GenreRepository extends ReactiveCrudRepository<Genre, Long> {
    @Query("""
        select g.id, g.name
        from genres g
        join books_genres bg on bg.genre_id = g.id
        where bg.book_id = :bookId
    """)
    Flux<Genre> findByBookId(@Param("bookId") Long bookId);

    @Query("""
        select g.id, g.name, bg.book_id as book_id
        from genres g
        join books_genres bg on bg.genre_id = g.id
        where bg.book_id is not null
    """)
    Flux<GenreWithBookIdDto> findAllWithBookId();
}

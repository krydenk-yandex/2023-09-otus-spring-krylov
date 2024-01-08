package ru.otus.hw11.repositories;

import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw11.models.Book;

public interface BookRepository extends ReactiveCrudRepository<Book, Long> {
    Flux<Book> findAll();

    @Modifying
    @Query("update books set author_id = :authorId, title = :title where id = :bookId")
    Mono<Void> update(
            @Param("bookId") long bookId,
            @Param("authorId") long authorId,
            @Param("title") String title
    );

    @Query("insert into books (author_id, title) values (:authorId, :title) returning id")
    Mono<Long> insert(
            @Param("authorId") long authorId,
            @Param("title") String title
    );

    @Modifying
    @Query("delete from books_genres where book_id = :bookId")
    Mono<Void> deleteBookGenres(@Param("bookId") long bookId);

    @Modifying
    @Query("insert into books_genres (book_id, genre_id) values (:bookId, :genreId)")
    Mono<Void> insertBookGenre(@Param("bookId") long bookId, @Param("genreId") long genreId);
}

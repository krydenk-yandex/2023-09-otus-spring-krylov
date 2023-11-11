package ru.otus.hw5.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw5.models.Author;
import ru.otus.hw5.models.Book;
import ru.otus.hw5.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
@RequiredArgsConstructor
public class BookRepositoryJdbc implements BookRepository {

    private final NamedParameterJdbcOperations jdbc;

    private final GenreRepository genreRepository;

    @Override
    public Optional<Book> findById(long id) {
        return findAll().stream().filter(book -> book.getId() == id).findFirst();
    }

    @Override
    public List<Book> findAll() {
        var genres = genreRepository.findAll();
        var relations = getAllGenreRelations();
        var books = getAllBooksWithoutGenres();
        mergeBooksInfo(books, genres, relations);
        return books;
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            return insert(book);
        }
        return update(book);
    }

    @Override
    public void deleteById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        jdbc.update("delete from books where id = :id", params);
    }

    private List<Book> getAllBooksWithoutGenres() {
        return jdbc.query(
                "select b.id, b.title, b.author_id, a.full_name as author_name " +
                        "from books b join authors a on a.id = b.author_id",
                new BookRowMapper());
    }

    private List<BookGenreRelation> getAllGenreRelations() {
        return jdbc.query(
                "select book_id, genre_id from books_genres",
                new BookGenreRowMapper());

    }

    private void mergeBooksInfo(List<Book> booksWithoutGenres, List<Genre> genres,
                                List<BookGenreRelation> relations) {
        Map<Long, Book> booksMap = booksWithoutGenres.stream().collect(
                Collectors.toMap(Book::getId, Function.identity())
        );
        Map<Long, Genre> genresMap = genres.stream().collect(
                Collectors.toMap(Genre::getId, Function.identity())
        );

        relations.forEach(relation -> {
            var book = booksMap.get(relation.bookId());
            var genre = genresMap.get(relation.genreId());

            if (book != null && genre != null) {
                if (book.getGenres() != null) {
                    book.setGenres(Stream.of(book.getGenres(), List.of(genre))
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList()));
                } else {
                    book.setGenres(List.of(genre));
                }
            }
        });
    }

    private Book insert(Book book) {
        var keyHolder = new GeneratedKeyHolder();

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("title", book.getTitle());
        params.addValue("author", book.getAuthor().getId());

        jdbc.update(
            "insert into books (title, author_id) values (:title, :author);",
                params,
                keyHolder
        );

        //noinspection DataFlowIssue
        book.setId(keyHolder.getKeyAs(Long.class));
        batchInsertGenresRelationsFor(book);
        return book;
    }

    private Book update(Book book) {
        Map<String, Object> params = Map.of(
                "book", book.getId(),
                "title", book.getTitle(),
                "author", book.getAuthor().getId()
        );

        jdbc.update(
            "update books set title = :title, author_id = :author where id = :book",
            params
        );

        removeGenresRelationsFor(book);
        batchInsertGenresRelationsFor(book);

        return book;
    }

    private void batchInsertGenresRelationsFor(Book book) {
        StringBuffer query = new StringBuffer();
        Map<String, Object> params = new HashMap();
        params.put("book", book.getId());

        book.getGenres().forEach(genre -> {
            query
                .append("insert into books_genres (book_id, genre_id) values (:book, :genre")
                .append(genre.getId()).append(");");
            params.put("genre" + genre.getId(), genre.getId());
        });

        jdbc.update(String.valueOf(query), params);
    }

    private void removeGenresRelationsFor(Book book) {
        Map<String, Object> params = Map.of("book", book.getId());
        jdbc.update("delete from books_genres where book_id = :book", params);
    }

    private static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            var book = new Book();
            book.setId(rs.getLong("id"));
            book.setTitle(rs.getString("title"));
            book.setAuthor(
                new Author(
                    rs.getLong("author_id"),
                    rs.getString("author_name")
                )
            );

            return book;
        }
    }

    private static class BookGenreRowMapper implements RowMapper<BookGenreRelation> {

        @Override
        public BookGenreRelation mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new BookGenreRelation(
                rs.getLong("book_id"),
                rs.getLong("genre_id")
            );
        }
    }

    private record BookGenreRelation(long bookId, long genreId) {}
}

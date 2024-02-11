package ru.otus.hw14.service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

import ru.otus.hw14.models.AuthorJdbc;
import ru.otus.hw14.models.AuthorMongo;
import ru.otus.hw14.models.BookJdbc;
import ru.otus.hw14.models.BookMongo;
import ru.otus.hw14.models.CommentJdbc;
import ru.otus.hw14.models.CommentMongo;
import ru.otus.hw14.models.GenreJdbc;
import ru.otus.hw14.models.GenreMongo;

public class MongoToRelationalService {

    private volatile AtomicLong authorIdSequence = new AtomicLong(0L);

    private ConcurrentMap<String, Long> authorIdsMap = new ConcurrentHashMap<>();

    private volatile AtomicLong genreIdSequence = new AtomicLong(0L);

    private ConcurrentMap<String, Long> genreIdsMap = new ConcurrentHashMap<>();

    private volatile AtomicLong bookIdSequence = new AtomicLong(0L);

    private ConcurrentMap<String, Long> bookIdsMap = new ConcurrentHashMap<>();

    private volatile AtomicLong commentIdSequence = new AtomicLong(0L);

    public AuthorJdbc convertAuthor(AuthorMongo author) {
        var currentAuthorId = authorIdSequence.incrementAndGet();

        authorIdsMap.put(author.getId(), currentAuthorId);

        return new AuthorJdbc(
                currentAuthorId,
                author.getFullName()
        );
    }

    public GenreJdbc convertGenre(GenreMongo genre) {
        var currentGenreId = genreIdSequence.incrementAndGet();

        genreIdsMap.put(genre.getId(), currentGenreId);

        return new GenreJdbc(
                currentGenreId,
                genre.getName()
        );
    }

    public BookJdbc convertBook(BookMongo book) {
        var currentBookId = bookIdSequence.incrementAndGet();

        bookIdsMap.put(book.getId(), currentBookId);

        return new BookJdbc(
                currentBookId,
                book.getTitle(),
                authorIdsMap.get(book.getAuthor().getId()),
                genreIdsMap.get(book.getGenre().getId())
        );
    }

    public CommentJdbc convertComment(CommentMongo comment) {
        var currentCommentId = commentIdSequence.incrementAndGet();

        return new CommentJdbc(
                currentCommentId,
                comment.getText(),
                comment.getAuthorName(),
                bookIdsMap.get(comment.getBook().getId())
        );
    }
}

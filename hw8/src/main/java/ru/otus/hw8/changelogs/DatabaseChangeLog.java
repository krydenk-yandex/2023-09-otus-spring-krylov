package ru.otus.hw8.changelogs;

import java.util.ArrayList;
import java.util.List;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.otus.hw8.models.Author;
import ru.otus.hw8.models.Book;
import ru.otus.hw8.models.Comment;
import ru.otus.hw8.models.Genre;
import ru.otus.hw8.repositories.AuthorRepository;
import ru.otus.hw8.repositories.BookRepository;
import ru.otus.hw8.repositories.CommentRepository;
import ru.otus.hw8.repositories.GenreRepository;

@ChangeLog(order = "001")
public class DatabaseChangeLog {

    private List<Author> authors = new ArrayList();

    private List<Genre> genres = new ArrayList();

    private List<Book> books = new ArrayList();

    @ChangeSet(order = "000", id = "dropDB", author = "krydenk", runAlways = true)
    public void dropDB(MongoDatabase database) {
        database.drop();
    }

    @ChangeSet(order = "001", id = "initAuthors", author = "krydenk", runAlways = true)
    public void initAuthors(AuthorRepository repository) {
        authors.add(repository.save(new Author("1", "Author_1")));
        authors.add(repository.save(new Author("2", "Author_2")));
        authors.add(repository.save(new Author("3", "Author_3")));
    }

    @ChangeSet(order = "002", id = "initGenres", author = "krydenk", runAlways = true)
    public void initGenres(GenreRepository repository) {
        genres.add(repository.save(new Genre("1", "Genre_1")));
        genres.add(repository.save(new Genre("2", "Genre_2")));
        genres.add(repository.save(new Genre("3", "Genre_3")));
        genres.add(repository.save(new Genre("4", "Genre_4")));
        genres.add(repository.save(new Genre("5", "Genre_5")));
        genres.add(repository.save(new Genre("6", "Genre_6")));
    }

    @ChangeSet(order = "003", id = "initBooks", author = "krydenk", runAlways = true)
    public void initBooks(BookRepository repository) {
        books.add(repository.save(new Book("1", "Book_1", authors.get(0), List.of(
                genres.get(0), genres.get(1)
        ))));
        books.add(repository.save(new Book("2", "Book_2", authors.get(1), List.of(
                genres.get(2), genres.get(3)
        ))));
        books.add(repository.save(new Book("3", "Book_3", authors.get(2), List.of(
                genres.get(4), genres.get(5)
        ))));
    }

    @ChangeSet(order = "004", id = "initComments", author = "krydenk", runAlways = true)
    public void initComments(CommentRepository repository) {
        repository.save(new Comment("1", "Comment_1", "CommentAuthor_1", books.get(0)));
        repository.save(new Comment("2", "Comment_2", "CommentAuthor_2", books.get(1)));
        repository.save(new Comment("3", "Comment_3", "CommentAuthor_3", books.get(2)));
    }
}

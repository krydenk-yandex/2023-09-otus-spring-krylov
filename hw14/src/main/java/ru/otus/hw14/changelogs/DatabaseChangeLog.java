package ru.otus.hw14.changelogs;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.otus.hw14.models.AuthorMongo;
import ru.otus.hw14.models.BookMongo;
import ru.otus.hw14.models.CommentMongo;
import ru.otus.hw14.models.GenreMongo;
import ru.otus.hw14.repositories.AuthorMongoRepository;
import ru.otus.hw14.repositories.BookMongoRepository;
import ru.otus.hw14.repositories.CommentMongoRepository;
import ru.otus.hw14.repositories.GenreRepository;

@ChangeLog(order = "001")
public class DatabaseChangeLog {
    private final List<Integer> range = IntStream.range(0, 1000).boxed().toList();

    private List<AuthorMongo> authors = new ArrayList();

    private List<GenreMongo> genres = new ArrayList();

    private List<BookMongo> books = new ArrayList();

    @ChangeSet(order = "000", id = "dropDB", author = "krydenk", runAlways = true)
    public void dropDB(MongoDatabase database) {
        database.drop();
    }

    @ChangeSet(order = "001", id = "initAuthors", author = "krydenk", runAlways = true)
    public void initAuthors(AuthorMongoRepository repository) {
        range.forEach(index ->
                authors.add(repository.save(new AuthorMongo(null, "Author_" + index))));
    }
    
    @ChangeSet(order = "002", id = "initGenres", author = "krydenk", runAlways = true)
    public void initGenres(GenreRepository repository) {
        range.forEach(index ->
                genres.add(repository.save(new GenreMongo(null, "Genre_" + index))));
    }

    @ChangeSet(order = "003", id = "initBooks", author = "krydenk", runAlways = true)
    public void initBooks(BookMongoRepository repository) {
        range.forEach(index ->
                books.add(repository.save(
                        new BookMongo(null, "Book_" + index, authors.get(index), genres.get(index)))));
    }

    @ChangeSet(order = "004", id = "initComments", author = "krydenk", runAlways = true)
    public void initComments(CommentMongoRepository repository) {
        range.forEach(index ->
                repository.save(new CommentMongo(
                        null,
                        "Comment_" + index,
                        "CommentAuthor_" + index,
                        books.get(index))));

    }
}

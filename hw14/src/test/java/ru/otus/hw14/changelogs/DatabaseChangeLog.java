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
    private final List<Integer> range = IntStream.range(0, 20).boxed().toList();

    private List<AuthorMongo> authors = new ArrayList();

    private List<GenreMongo> genres = new ArrayList();

    private List<BookMongo> books = new ArrayList();

    @ChangeSet(order = "000", id = "dropDB", author = "krydenk", runAlways = true)
    public void dropDB(MongoDatabase database) {
        database.drop();
    }

    @ChangeSet(order = "001", id = "initAuthors", author = "krydenk", runAlways = true)
    public void initAuthors(AuthorMongoRepository repository) {
        authors.add(repository.save(new AuthorMongo("1", "Author_1")));
        authors.add(repository.save(new AuthorMongo("2", "Author_2")));
    }
    
    @ChangeSet(order = "002", id = "initGenres", author = "krydenk", runAlways = true)
    public void initGenres(GenreRepository repository) {
        genres.add(repository.save(new GenreMongo("1", "Genre_1")));
        genres.add(repository.save(new GenreMongo("2", "Genre_2")));
    }

    @ChangeSet(order = "003", id = "initBooks", author = "krydenk", runAlways = true)
    public void initBooks(BookMongoRepository repository) {
        books.add(repository.save(new BookMongo("1", "Book_1", authors.get(0), genres.get(0))));
        books.add(repository.save(new BookMongo("2", "Book_2", authors.get(1), genres.get(1))));
    }

    @ChangeSet(order = "004", id = "initComments", author = "krydenk", runAlways = true)
    public void initComments(CommentMongoRepository repository) {
        repository.save(new CommentMongo("1", "Comment_1", "CommentAuthor_1", books.get(0)));
        repository.save(new CommentMongo("2", "Comment_2", "CommentAuthor_1", books.get(0)));
        repository.save(new CommentMongo("3", "Comment_3", "CommentAuthor_2", books.get(1)));
        repository.save(new CommentMongo("4", "Comment_4", "CommentAuthor_2", books.get(1)));
    }
}

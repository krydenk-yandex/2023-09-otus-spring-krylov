package ru.otus.hw14.jobs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.otus.hw14.mappers.BookRowMapper;
import ru.otus.hw14.mappers.CommentRowMapper;
import ru.otus.hw14.models.BookJdbcTest;
import ru.otus.hw14.models.BookMongo;
import ru.otus.hw14.models.CommentJdbcTest;
import ru.otus.hw14.models.CommentMongo;
import ru.otus.hw14.repositories.BookMongoRepository;
import ru.otus.hw14.repositories.CommentMongoRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.otus.hw14.jobs.MongoToRelationalLibraryImportJobConfig.IMPORT_LIBRARY_JOB_NAME;

@SpringBootTest
@SpringBatchTest
@DisplayName("Импорт данных библиотеки из Mongo в реляционную БД ")
class MongoToRelationalLibraryImportJobConfigTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;

    @Autowired
    private BookMongoRepository bookMongoRepository;

    @Autowired
    private CommentMongoRepository commentMongoRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @DisplayName("должен проходить корректно")
    void testImport() throws Exception {
        Job job = jobLauncherTestUtils.getJob();
        assertThat(job).isNotNull()
                .extracting(Job::getName)
                .isEqualTo(IMPORT_LIBRARY_JOB_NAME);

        Map<String, JobParameter<?>> properties = new HashMap<>();
        properties.put("jobKey", new JobParameter<String>("whatever", String.class));
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(new JobParameters(properties));

        assertThat(jobExecution.getExitStatus().getExitCode()).isEqualTo("COMPLETED");

        var booksMongo = bookMongoRepository.findAll();
        assertThat(booksMongo).hasSize(2);

        var booksJdbc = jdbcTemplate.query("""
                    select b.id, b.title, a.full_name as author_name, g.name as genre_name from books b
                    join authors a on b.author_id = a.id
                    join genres g on g.id = b.genre_id
                """,
                new BookRowMapper()
        );
        assertThat(booksJdbc).hasSize(2);
        assertThat(booksJdbc).allMatch(bookJdbc -> {
            var maybeBookMongo = findCorresponding(bookJdbc, booksMongo);
            assertThat(maybeBookMongo).isPresent();

            var bookMongo = maybeBookMongo.get();

            return bookMongo.getAuthor().getFullName().equals(String.valueOf(bookJdbc.getAuthorName()))
                    && bookMongo.getGenre().getName().equals(String.valueOf(bookJdbc.getGenreName()));
        });

        var commentsMongo = commentMongoRepository.findAll();
        assertThat(commentsMongo).hasSize(4);

        var commentsJdbc = jdbcTemplate.query("""
                    select c.id, c.text, c.author_name, b.title as book_title from comments c
                    join books b on c.book_id = b.id
                """,
                new CommentRowMapper()
        );

        assertThat(commentsJdbc).allMatch(commentJdbc -> {
            var maybeCommentMongo = findCorresponding(commentJdbc, commentsMongo);
            assertThat(maybeCommentMongo).isPresent();

            var commentMongo = maybeCommentMongo.get();

            return commentMongo.getBook().getTitle().equals(String.valueOf(commentJdbc.getBookTitle()))
                    && commentMongo.getAuthorName().equals(String.valueOf(commentJdbc.getAuthorName()));
        });
    }

    private Optional<BookMongo> findCorresponding(BookJdbcTest bookJdbc, List<BookMongo> booksMongo) {
        return booksMongo.stream().filter(bookMongo -> bookJdbc.getTitle().equals(bookMongo.getTitle())).findAny();
    }

    private Optional<CommentMongo> findCorresponding(CommentJdbcTest commentJdbc, List<CommentMongo> commentsMongo) {
        return commentsMongo.stream().filter(commentMongo -> commentJdbc.getText().equals(commentMongo.getText())).findAny();
    }
}
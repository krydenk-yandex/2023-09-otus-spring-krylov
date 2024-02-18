package ru.otus.hw16.actuator;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import ru.otus.hw16.repositories.BookRepository;

@Component
@AllArgsConstructor
public class BooksExistenceIndicator implements HealthIndicator {
    private BookRepository bookRepository;

    @Override
    public Health health() {
        var areAnyBooksInDb = bookRepository.count() > 0;

        if (!areAnyBooksInDb) {
            return Health.down()
                    .status(Status.DOWN)
                    .withDetail("message", "В БД нет ни одной книги - что-то не так!")
                    .build();
        } else {
            return Health.up().withDetail("message", "В БД есть какие-то книги, все хорошо").build();
        }
    }
}

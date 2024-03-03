package ru.otus.hw18.resillence;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import ru.otus.hw18.repositories.BookRepository;
import ru.otus.hw18.services.BookService;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;

@SpringBootTest
@DisplayName("Сервис работы с книгами ")
public class BookServiceCircuitBreaker {

    @Autowired
    private BookService bookService;

    @Autowired
    @SpyBean
    private BookRepository repository;

    @DisplayName(" должен получить исключение от circuitBreaker при повторной ошибке в репозитории")
    @Test
    public void shouldEnableCircuitBreaker() throws InterruptedException {
        doThrow(new IllegalStateException()).when(repository).findAll();

        assertThrows(IllegalStateException.class, () -> bookService.findAll());
        // circuit breaker is on
        assertThrows(CallNotPermittedException.class, () -> bookService.findAll());

        Thread.sleep(5000 + 1);

        // circuit breaker is off
        assertThrows(IllegalStateException.class, () -> bookService.findAll());
    }
}

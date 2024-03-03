package ru.otus.hw18.resillence;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import ru.otus.hw18.repositories.AuthorRepository;
import ru.otus.hw18.services.AuthorService;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;

@SpringBootTest
@DisplayName("Сервис работы с авторами ")
public class AuthorServiceCircuitBreaker {

    @Autowired
    private AuthorService authorService;

    @Autowired
    @SpyBean
    private AuthorRepository repository;

    @DisplayName(" должен получить исключение от circuitBreaker при повторной ошибке в репозитории")
    @Test
    public void shouldEnableCircuitBreaker() throws InterruptedException {
        doThrow(new IllegalStateException()).when(repository).findAll();

        assertThrows(IllegalStateException.class, () -> authorService.findAll());
        // circuit breaker is on
        assertThrows(CallNotPermittedException.class, () -> authorService.findAll());

        Thread.sleep(5000 + 1);

        // circuit breaker is off
        assertThrows(IllegalStateException.class, () -> authorService.findAll());
    }
}

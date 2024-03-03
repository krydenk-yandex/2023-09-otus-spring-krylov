package ru.otus.hw18.exceptions;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerAdvice {
    @ExceptionHandler(CallNotPermittedException.class)
    public ResponseEntity<String> defaultCircuitBreakerHandler(CallNotPermittedException ex) {
        return ResponseEntity.badRequest().body("Похоже, в данный момент сервис перегружен, зайдите позже");
    }
}

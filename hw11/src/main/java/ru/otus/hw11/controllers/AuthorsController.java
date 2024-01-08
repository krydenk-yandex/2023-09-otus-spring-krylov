package ru.otus.hw11.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.otus.hw11.models.Author;
import ru.otus.hw11.repositories.AuthorRepository;

@RestController
@RequiredArgsConstructor
public class AuthorsController {

    private final AuthorRepository authorRepository;

    @GetMapping("/api/authors")
    public Flux<Author> getAuthors() {
        return authorRepository.findAll();
    }
}
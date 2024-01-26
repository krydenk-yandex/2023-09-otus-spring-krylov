package ru.otus.hw11.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.otus.hw11.models.Genre;
import ru.otus.hw11.repositories.GenreRepository;

@RestController
@RequiredArgsConstructor
public class GenresController {

    private final GenreRepository genreRepository;

    @GetMapping("/api/genres")
    public Flux<Genre> getAuthors() {
        return genreRepository.findAll();
    }
}
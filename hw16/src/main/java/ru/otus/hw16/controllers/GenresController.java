package ru.otus.hw16.controllers;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw16.models.Genre;
import ru.otus.hw16.services.GenreService;

@RestController
@RequiredArgsConstructor
public class GenresController {
    private final GenreService genreService;

    @GetMapping("/api/genres")
    public List<Genre> genresList() {
        return genreService.findAll();
    }
}

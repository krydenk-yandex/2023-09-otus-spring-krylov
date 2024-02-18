package ru.otus.hw16.controllers;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw16.models.Author;
import ru.otus.hw16.services.AuthorService;

@RestController
@RequiredArgsConstructor
public class AuthorController {
    private final AuthorService authorService;

    @GetMapping("/api/authors")
    public List<Author> authorsList() {
        return authorService.findAll();
    }
}

package ru.otus.hw8.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.command.annotation.Option;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw8.converters.GenreConverter;
import ru.otus.hw8.models.Genre;
import ru.otus.hw8.services.GenreService;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@ShellComponent
public class GenreCommands {

    private final GenreService genreService;

    private final GenreConverter genreConverter;

    @ShellMethod(value = "Find all genres", key = "ag")
    public String findAllGenres() {
        return genreService.findAll().stream()
                .map(genreConverter::genreToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @ShellMethod(value = "Find all genres by ids", key = "agbids")
    public String findAllGenresByIds(@Option(longNames = "ids") List<String> ids) {
        return genreService.findAllByIds(ids).stream()
                .map(genreConverter::genreToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @ShellMethod(value = "Save genre", key = "gs")
    public String save(String name, String id) {
        var genre = new Genre(id, name);
        return genreConverter.genreToString(genreService.save(genre));
    }

    @ShellMethod(value = "Delete genre", key = "gd")
    public void delete(String id) {
        genreService.deleteById(id);
    }
}

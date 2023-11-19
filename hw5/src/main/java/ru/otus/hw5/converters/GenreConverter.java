package ru.otus.hw5.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw5.models.Genre;

@Component
public class GenreConverter {
    public String genreToString(Genre genre) {
        return "Id: %d, Name: %s".formatted(genre.getId(), genre.getName());
    }
}

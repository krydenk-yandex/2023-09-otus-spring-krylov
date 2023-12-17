package ru.otus.hw8.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw8.dto.GenreDto;
import ru.otus.hw8.models.Genre;

@Component
public class GenreConverter {
    public String genreToString(Genre genre) {
        return "Id: %s, Name: %s".formatted(genre.getId(), genre.getName());
    }

    public String genreDtoToString(GenreDto genre) {
        return "Id: %s, Name: %s".formatted(genre.getId(), genre.getName());
    }

    public GenreDto toDto(Genre genre) {
        return new GenreDto(
                genre.getId(),
                genre.getName()
        );
    }
}

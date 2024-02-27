package ru.otus.hw13.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw13.dto.GenreDto;
import ru.otus.hw13.models.Genre;

@Component
public class GenreConverter {
    public GenreDto toDto(Genre genre) {
        return new GenreDto(
                genre.getId(),
                genre.getName()
        );
    }
}

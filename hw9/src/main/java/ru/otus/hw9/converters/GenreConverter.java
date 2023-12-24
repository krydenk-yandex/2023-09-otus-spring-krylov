package ru.otus.hw9.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw9.dto.GenreDto;
import ru.otus.hw9.models.Genre;

@Component
public class GenreConverter {
    public GenreDto toDto(Genre genre) {
        return new GenreDto(
                genre.getId(),
                genre.getName()
        );
    }
}

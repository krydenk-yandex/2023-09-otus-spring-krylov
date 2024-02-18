package ru.otus.hw16.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw16.dto.GenreDto;
import ru.otus.hw16.models.Genre;

@Component
public class GenreConverter {
    public GenreDto toDto(Genre genre) {
        return new GenreDto(
                genre.getId(),
                genre.getName()
        );
    }
}

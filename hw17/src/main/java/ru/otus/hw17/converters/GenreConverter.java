package ru.otus.hw17.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw17.dto.GenreDto;
import ru.otus.hw17.models.Genre;

@Component
public class GenreConverter {
    public GenreDto toDto(Genre genre) {
        return new GenreDto(
                genre.getId(),
                genre.getName()
        );
    }
}

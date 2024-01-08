package ru.otus.hw11.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw11.dto.GenreWithBookIdDto;
import ru.otus.hw11.models.Genre;

@Component
public class GenreConverter {
    public Genre toEntity(GenreWithBookIdDto genreDto) {
        return new Genre(
                genreDto.getId(),
                genreDto.getName()
        );
    }
}

package ru.otus.library.converters;

import org.springframework.stereotype.Component;
import ru.otus.library.dto.GenreDto;
import ru.otus.library.models.Genre;

@Component
public class GenreConverter {
    public GenreDto toDto(Genre genre) {
        return new GenreDto(
                genre.getId(),
                genre.getName()
        );
    }
}

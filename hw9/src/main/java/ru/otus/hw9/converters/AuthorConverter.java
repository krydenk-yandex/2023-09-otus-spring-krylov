package ru.otus.hw9.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw9.dto.AuthorDto;
import ru.otus.hw9.models.Author;

@Component
public class AuthorConverter {
    public AuthorDto toDto(Author author) {
        return new AuthorDto(
                author.getId(),
                author.getFullName()
        );
    }
}

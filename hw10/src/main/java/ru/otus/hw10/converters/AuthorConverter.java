package ru.otus.hw10.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw10.dto.AuthorDto;
import ru.otus.hw10.models.Author;

@Component
public class AuthorConverter {
    public AuthorDto toDto(Author author) {
        return new AuthorDto(
                author.getId(),
                author.getFullName()
        );
    }
}

package ru.otus.hw12.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw12.dto.AuthorDto;
import ru.otus.hw12.models.Author;

@Component
public class AuthorConverter {
    public AuthorDto toDto(Author author) {
        return new AuthorDto(
                author.getId(),
                author.getFullName()
        );
    }
}

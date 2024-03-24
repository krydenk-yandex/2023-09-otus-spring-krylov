package ru.otus.library.converters;

import org.springframework.stereotype.Component;
import ru.otus.library.dto.AuthorDto;
import ru.otus.library.models.Author;

@Component
public class AuthorConverter {
    public AuthorDto toDto(Author author) {
        return new AuthorDto(
                author.getId(),
                author.getFullName()
        );
    }
}

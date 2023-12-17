package ru.otus.hw8.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw8.dto.AuthorDto;
import ru.otus.hw8.models.Author;

@Component
public class AuthorConverter {
    public String authorToString(Author author) {
        return "Id: %s, FullName: %s".formatted(author.getId(), author.getFullName());
    }

    public String authorDtoToString(AuthorDto author) {
        return "Id: %s, FullName: %s".formatted(author.getId(), author.getFullName());
    }

    public AuthorDto toDto(Author author) {
        return new AuthorDto(
                author.getId(),
                author.getFullName()
        );
    }
}

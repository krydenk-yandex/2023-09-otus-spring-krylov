package ru.otus.hw11.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw11.dto.AuthorWithBookIdDto;
import ru.otus.hw11.models.Author;

@Component
public class AuthorConverter {
    public Author toEntity(AuthorWithBookIdDto authorDto) {
        return new Author(
                authorDto.getId(),
                authorDto.getFullName()
        );
    }
}

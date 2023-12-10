package ru.otus.hw6.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw6.models.Author;

@Component
public class AuthorConverter {
    public String authorToString(Author author) {
        return "Id: %d, FullName: %s".formatted(author.getId(), author.getFullName());
    }
}

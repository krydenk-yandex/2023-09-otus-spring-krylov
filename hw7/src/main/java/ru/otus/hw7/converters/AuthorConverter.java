package ru.otus.hw7.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw7.models.Author;

@Component
public class AuthorConverter {
    public String authorToString(Author author) {
        return "Id: %d, FullName: %s".formatted(author.getId(), author.getFullName());
    }
}

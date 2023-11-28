package ru.otus.hw7.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw7.models.Comment;

@Component
public class CommentConverter {
    public String authorToString(Comment comment) {
        return "Id: %d, Text: %s, AuthorName: %s".formatted(
            comment.getId(),
            comment.getText(),
            comment.getAuthorName()
        );
    }
}

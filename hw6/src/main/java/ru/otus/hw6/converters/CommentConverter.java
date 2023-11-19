package ru.otus.hw6.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw6.models.Comment;

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

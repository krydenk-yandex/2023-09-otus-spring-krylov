package ru.otus.hw8.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw8.models.Comment;

@Component
public class CommentConverter {
    public String commentToString(Comment comment) {
        return ("Id: %s, Text: %s, AuthorName: %s, " +
                "Book: { Id: %s, Title: %s }").formatted(
                comment.getId(),
                comment.getText(),
                comment.getAuthorName(),
                comment.getBook().getId(),
                comment.getBook().getTitle()
        );
    }
}

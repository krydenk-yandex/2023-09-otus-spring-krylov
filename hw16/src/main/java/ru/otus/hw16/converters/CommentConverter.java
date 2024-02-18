package ru.otus.hw16.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw16.models.Comment;

@Component
public class CommentConverter {
    public String commentToString(Comment comment) {
        return ("Id: %d, Text: %s, AuthorName: %s, " +
                "Book: { Id: %s, Title: %s }").formatted(
                comment.getId(),
                comment.getText(),
                comment.getAuthorName(),
                comment.getBook().getId(),
                comment.getBook().getTitle()
        );
    }
}
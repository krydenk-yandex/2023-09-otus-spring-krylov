package ru.otus.hw6.commands;

import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.command.annotation.Option;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw6.converters.CommentConverter;
import ru.otus.hw6.services.CommentService;

@RequiredArgsConstructor
@ShellComponent
public class CommentCommands {

    private final CommentService commentService;

    private final CommentConverter commentConverter;

    @ShellMethod(value = "Insert comment", key = "cins")
    public String createComment(String text, String authorName, long bookId) {
        var comment = commentService.insert(text, authorName, bookId);
        return commentConverter.commentToString(comment);
    }

    @ShellMethod(value = "Update comment", key = "cupd")
    public String updateComment(long id, String text, String authorName, long bookId) {
        var comment = commentService.update(id, text, authorName, bookId);
        return commentConverter.commentToString(comment);
    }

    @ShellMethod(value = "Delete comment by id", key = "cdi")
    public void deleteCommentById(@Option(longNames = "id") long id) {
        commentService.deleteById(id);
    }

    @ShellMethod(value = "Find comment by id", key = "cbi")
    public String findCommentById(@Option(longNames = "id") long id) {
        return commentService.findById(id).stream()
                .map(commentConverter::commentToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @ShellMethod(value = "Find comments by book id", key = "cbb")
    public String findCommentsByBookId(@Option(longNames = "book-id") long bookId) {
        return commentService.findByBookId(bookId).stream()
            .map(commentConverter::commentToString)
            .collect(Collectors.joining("," + System.lineSeparator()));
    }
}

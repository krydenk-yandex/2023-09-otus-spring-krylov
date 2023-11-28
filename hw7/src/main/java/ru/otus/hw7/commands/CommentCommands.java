package ru.otus.hw7.commands;

import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.command.annotation.Option;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw7.converters.CommentConverter;
import ru.otus.hw7.services.CommentService;

@RequiredArgsConstructor
@ShellComponent
public class CommentCommands {

    private final CommentService commentService;

    private final CommentConverter commentConverter;

    @ShellMethod(value = "Find comments by book id", key = "cbb")
    public String findCommentsByBookId(@Option(longNames = "book-id") long bookId) {
        return commentService.findByBookId(bookId).stream()
            .map(commentConverter::authorToString)
            .collect(Collectors.joining("," + System.lineSeparator()));
    }
}

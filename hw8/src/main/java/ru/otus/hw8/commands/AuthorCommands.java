package ru.otus.hw8.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw8.converters.AuthorConverter;
import ru.otus.hw8.models.Author;
import ru.otus.hw8.services.AuthorService;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@ShellComponent
public class AuthorCommands {

    private final AuthorService authorService;

    private final AuthorConverter authorConverter;

    @ShellMethod(value = "Find all authors", key = "aa")
    public String findAllAuthors() {
       return authorService.findAll().stream()
                .map(authorConverter::authorToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @ShellMethod(value = "Save author", key = "as")
    public String save(String fullName, String id) {
        var author = new Author(id, fullName);
        return authorConverter.authorToString(authorService.save(author));
    }

    @ShellMethod(value = "Delete author", key = "ad")
    public void delete(String id) {
        authorService.deleteById(id);
    }
}

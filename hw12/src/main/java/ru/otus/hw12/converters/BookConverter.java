package ru.otus.hw12.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw12.dto.BookDto;
import ru.otus.hw12.dto.BookSaveDto;
import ru.otus.hw12.models.Book;
import ru.otus.hw12.models.Genre;

@RequiredArgsConstructor
@Component
public class BookConverter {
    private final AuthorConverter authorConverter;

    private final GenreConverter genreConverter;

    public BookDto toDto(Book book) {
        return new BookDto(
                book.getId(),
                book.getTitle(),
                authorConverter.toDto(book.getAuthor()),
                book.getGenres().stream().map(genreConverter::toDto).toList()
        );
    }

    public BookSaveDto toSaveDto(Book book) {
        return new BookSaveDto(
                book.getTitle(),
                book.getGenres().stream().map(Genre::getId).toList(),
                book.getAuthor().getId()
        );
    }
}

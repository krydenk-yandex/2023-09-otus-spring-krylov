package ru.otus.library.converters;

import java.util.Comparator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.library.dto.BookDto;
import ru.otus.library.dto.BookReducedDto;
import ru.otus.library.dto.BookWithChaptersDto;
import ru.otus.library.models.Book;
import ru.otus.library.models.Chapter;

@RequiredArgsConstructor
@Component
public class BookConverter {
    private final AuthorConverter authorConverter;

    private final GenreConverter genreConverter;

    private final ChapterConverter chapterConverter;

    public BookDto toDto(Book book) {
        return new BookDto(
                book.getId(),
                book.getTitle(),
                authorConverter.toDto(book.getAuthor()),
                book.getGenres().stream().map(genreConverter::toDto).toList()
        );
    }

    public BookWithChaptersDto toDtoWithChapters(Book book) {
        return new BookWithChaptersDto(
                book.getId(),
                book.getTitle(),
                authorConverter.toDto(book.getAuthor()),
                book.getGenres().stream().map(genreConverter::toDto).toList(),
                book.getChapters().stream()
                        .sorted(Comparator.comparingInt(Chapter::getOrderNumber))
                        .map(c -> chapterConverter.toDto(c, toReducedDto(book)))
                        .toList()
        );
    }

    public BookReducedDto toReducedDto(Book book) {
        return new BookReducedDto(
                book.getId(),
                book.getTitle()
        );
    }
}

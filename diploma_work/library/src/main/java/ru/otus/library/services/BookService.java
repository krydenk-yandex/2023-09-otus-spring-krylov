package ru.otus.library.services;

import ru.otus.library.dto.BookDto;
import ru.otus.library.dto.BookWithChaptersDto;
import ru.otus.library.dto.ChapterSaveDto;

import java.util.List;
import java.util.Optional;

public interface BookService {
    boolean existById(long id);

    Optional<BookWithChaptersDto> findById(long id);

    List<BookDto> findAll();

    BookWithChaptersDto insert(String title, long authorId, List<Long> genresIds, List<ChapterSaveDto> chapters);

    BookWithChaptersDto update(long id, String title, long authorId, List<Long> genresIds, List<ChapterSaveDto> chapters);

    void deleteById(long id);
}

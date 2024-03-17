package ru.otus.library.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.library.converters.BookConverter;
import ru.otus.library.converters.ChapterConverter;
import ru.otus.library.dto.BookDto;
import ru.otus.library.dto.BookWithChaptersDto;
import ru.otus.library.dto.ChapterSaveDto;
import ru.otus.library.exceptions.EntityNotFoundException;
import ru.otus.library.models.Book;
import ru.otus.library.repositories.AuthorRepository;
import ru.otus.library.repositories.BookRepository;
import ru.otus.library.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;

import static org.springframework.util.CollectionUtils.isEmpty;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final ChapterConverter chapterConverter;

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final BookConverter bookConverter;

    @Override
    public boolean existById(long id) {
        return bookRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BookWithChaptersDto> findById(long id) {
        return bookRepository.findById(id).map(bookConverter::toDtoWithChapters);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookDto> findAll() {
        return bookRepository.findAll()
                .stream()
                .map(bookConverter::toDto)
                .toList();
    }

    @Transactional
    @Override
    public BookWithChaptersDto insert(String title, long authorId, List<Long> genresIds, List<ChapterSaveDto> chapters) {
        return bookConverter.toDtoWithChapters(
                save(0, title, authorId, genresIds, chapters)
        );
    }

    @Transactional
    @Override
    public BookWithChaptersDto update(long id, String title, long authorId, List<Long> genresIds, List<ChapterSaveDto> chapters) {
        return bookConverter.toDtoWithChapters(
                save(id, title, authorId, genresIds, chapters)
        );
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        bookRepository.deleteById(id);
    }

    private Book save(long id, String title, long authorId, List<Long> genresIds, List<ChapterSaveDto> chapterDtos) {
        var author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author with id %d not found".formatted(authorId)));
        var genres = genreRepository.findByIdIn(genresIds);
        if (isEmpty(genres)) {
            throw new EntityNotFoundException("Genres with ids %s not found".formatted(genresIds));
        }

        var book = new Book();
        book.setId(id);
        book.setTitle(title);
        book.setAuthor(author);
        book.setGenres(genres);

        book.setChapters(chapterConverter.toEntities(
                chapterDtos,
                book
        ));

        return bookRepository.save(book);
    }
}

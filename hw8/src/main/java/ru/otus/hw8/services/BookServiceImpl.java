package ru.otus.hw8.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw8.converters.BookConverter;
import ru.otus.hw8.dto.BookDto;
import ru.otus.hw8.projections.IdProjection;
import ru.otus.hw8.exceptions.EntityNotFoundException;
import ru.otus.hw8.models.Book;
import ru.otus.hw8.repositories.AuthorRepository;
import ru.otus.hw8.repositories.BookRepository;
import ru.otus.hw8.repositories.CommentRepository;
import ru.otus.hw8.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;

import static org.springframework.util.CollectionUtils.isEmpty;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final CommentRepository commentRepository;

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final BookConverter bookConverter;

    @Override
    public Optional<Book> findById(String id) {
        return bookRepository.findById(id);
    }

    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll()
                .stream()
                .map(bookConverter::toDto)
                .toList();
    }

    @Override
    public Book insert(String title, String authorId, List<String> genresIds) {
        return save(null, title, authorId, genresIds);
    }

    @Override
    public Book update(String id, String title, String authorId, List<String> genresIds) {
        return save(id, title, authorId, genresIds);
    }

    @Override
    public void deleteById(String id) {
        commentRepository.deleteAllById(
                commentRepository.findIdsByBookId(id).stream()
                        .map(IdProjection::_id)
                        .toList()
        );
        bookRepository.deleteById(id);
    }

    private Book save(String id, String title, String authorId, List<String> genresIds) {
        var author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author with id %s not found".formatted(authorId)));
        var genres = genreRepository.findByIdIn(genresIds);
        if (isEmpty(genres)) {
            throw new EntityNotFoundException("Genres with ids %s not found".formatted(genresIds));
        }
        var book = new Book(id, title, author, genres);
        return bookRepository.save(book);
    }
}

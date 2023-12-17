package ru.otus.hw8.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw8.converters.BookConverter;
import ru.otus.hw8.dto.BookDto;
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
    @Transactional(readOnly = true)
    public Optional<Book> findById(String id) {
        return bookRepository.findById(id);
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
    public Book insert(String title, String authorId, List<String> genresIds) {
        return save(null, title, authorId, genresIds);
    }

    @Transactional
    @Override
    public Book update(String id, String title, String authorId, List<String> genresIds) {
        return save(id, title, authorId, genresIds);
    }

    @Transactional
    @Override
    public void deleteById(String id) {
        commentRepository.deleteAll(commentRepository.findByBookId(id));
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

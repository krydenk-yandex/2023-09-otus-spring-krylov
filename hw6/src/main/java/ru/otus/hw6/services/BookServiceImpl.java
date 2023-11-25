package ru.otus.hw6.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw6.exceptions.EntityNotFoundException;
import ru.otus.hw6.models.Book;
import ru.otus.hw6.repositories.AuthorRepository;
import ru.otus.hw6.repositories.BookRepository;
import ru.otus.hw6.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;

import static org.springframework.util.CollectionUtils.isEmpty;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<Book> findById(long id) {
        return bookRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Transactional
    @Override
    public Book insert(String title, long authorId, List<Long> genresIds) {
        return save(0, title, authorId, genresIds);
    }

    @Transactional
    @Override
    public Book update(long id, String title, long authorId, List<Long> genresIds) {
        return save(id, title, authorId, genresIds);
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        bookRepository.deleteById(id);
    }

    private Book save(long id, String title, long authorId, List<Long> genresIds) {
        var author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author with id %d not found".formatted(authorId)));
        var genres = genreRepository.findAllByIds(genresIds);
        if (isEmpty(genres)) {
            throw new EntityNotFoundException("Genres with ids %s not found".formatted(genresIds));
        }
        var book = new Book(id, title, author, genres);
        return bookRepository.save(book);
    }
}
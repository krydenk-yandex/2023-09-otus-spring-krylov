package ru.otus.hw8.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw8.exceptions.EntityDeletionForbidException;
import ru.otus.hw8.exceptions.EntityNotFoundException;
import ru.otus.hw8.models.Author;
import ru.otus.hw8.repositories.AuthorRepository;
import ru.otus.hw8.repositories.BookRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    private final BookRepository bookRepository;

    @Override
    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    @Override
    public Author save(Author author) {
        return authorRepository.save(author);
    }

    @Override
    public void deleteById(String id) {
        if (!authorRepository.existsById(id)) {
            throw new EntityNotFoundException("Author is not found on attempt to delete");
        }

        if (bookRepository.existsBookByAuthorId(id)) {
            throw new EntityDeletionForbidException(
                    "There are books related to the author with id %s, handle books first"
                            .formatted(id)
            );
        }
        authorRepository.deleteById(id);
    }
}

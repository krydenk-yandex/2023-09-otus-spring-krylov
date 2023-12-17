package ru.otus.hw8.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw8.exceptions.EntityDeletionForbidException;
import ru.otus.hw8.exceptions.EntityNotFoundException;
import ru.otus.hw8.models.Genre;
import ru.otus.hw8.repositories.BookRepository;
import ru.otus.hw8.repositories.GenreRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    @Override
    public List<Genre> findAll() {
        return genreRepository.findAll();
    }

    @Override
    public List<Genre> findAllByIds(List<String> ids) {
        return genreRepository.findByIdIn(ids);
    }

    @Override
    public Genre save(Genre genre) {
        return genreRepository.save(genre);
    }

    @Override
    public void deleteById(String id) {
        var theGenre = genreRepository.findById(id);
        if (theGenre.isEmpty()) {
            throw new EntityNotFoundException("Genre is not found on attempt to delete");
        }

        if (bookRepository.existsBookByGenresContains(List.of(theGenre.get()))) {
            throw new EntityDeletionForbidException(
                    "There are books related to genre with id %s, handle books first"
                            .formatted(id));
        }

        genreRepository.deleteById(id);
    }
}

package ru.otus.hw11.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw11.converters.AuthorConverter;
import ru.otus.hw11.converters.GenreConverter;
import ru.otus.hw11.dto.AuthorWithBookIdDto;
import ru.otus.hw11.dto.BookSaveDto;
import ru.otus.hw11.dto.GenreWithBookIdDto;
import ru.otus.hw11.models.Book;
import ru.otus.hw11.models.Genre;
import ru.otus.hw11.repositories.AuthorRepository;
import ru.otus.hw11.repositories.BookRepository;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw11.repositories.GenreRepository;

@RestController
@RequiredArgsConstructor
public class BookController {
    private final BookRepository bookRepository;

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final AuthorConverter authorConverter;

    private final GenreConverter genreConverter;

    @GetMapping("/api/books/{bookId}")
    public Mono<Book> getBookById(@PathVariable Long bookId) {
        return findBookById(bookId);
    }

    @GetMapping("/api/books")
    public Flux<Book> getBooks() {
        var booksFlux = bookRepository.findAll();
        var genresMapMono = getGenresByBookIdMap();
        var authorsMapMono = getAuthorsByBookIdMap();

        return booksFlux
                .flatMap(book -> Mono.just(book).zipWith(authorsMapMono))
                .map(tuple -> {
                    var book = tuple.getT1();
                    var authorsMap = tuple.getT2();
                    book.setAuthor(authorConverter.toEntity(
                            authorsMap.get(book.getId())
                    ));
                    return book;
                })
                .flatMap(book -> Mono.just(book).zipWith(genresMapMono))
                .map(tuple -> {
                    var book = tuple.getT1();
                    var genresMap = tuple.getT2();
                    book.setGenres(
                            genresMap.get(book.getId()).stream().map(genreConverter::toEntity).toList()
                    );
                    return book;
                });
    }

    @PutMapping("/api/books/{bookId}")
    public Mono<Book> editBook(
            @PathVariable Long bookId,
            BookSaveDto dto
    ) {
        Flux<Void> genreUpdateFlux =
                bookRepository.deleteBookGenres(bookId)
                        .thenMany(Flux.merge(
                                dto.getGenresIds().stream().map(
                                        genreId -> bookRepository.insertBookGenre(bookId, genreId)
                                ).toList()
                        ));

        return genreUpdateFlux
                .then(bookRepository.update(bookId, dto.getAuthorId(), dto.getTitle()))
                .then(findBookById(bookId));

    }

    @PostMapping("/api/books")
    public Mono<Book> insertBook(
            @Valid BookSaveDto dto
    ) {
        return bookRepository.insert(dto.getAuthorId(), dto.getTitle())
                .flatMap(bookId -> Flux.merge(dto.getGenresIds().stream().map(
                        genreId -> bookRepository.insertBookGenre(bookId, genreId)
                ).toList()).then(Mono.just(bookId)))
                .flatMap(this::findBookById);
    }

    @DeleteMapping("/api/books/{bookId}")
    public Mono<Void> deleteBook(@PathVariable Long bookId) {
        return bookRepository.existsById(bookId)
                .flatMap(found -> {
                    if (found) {
                        return bookRepository.deleteById(bookId);
                    }

                    return Mono.empty();
                });
    }

    private Mono<Map<Long, AuthorWithBookIdDto>> getAuthorsByBookIdMap() {
        return authorRepository.findAllWithBookId()
                .collect(
                        HashMap::new,
                        (map, author) -> map.put(author.getBookId(), author)
                );
    }

    private Mono<Map<Long, List<GenreWithBookIdDto>>> getGenresByBookIdMap() {
        return genreRepository.findAllWithBookId()
                .collect(
                        HashMap::new,
                        (map, genre) -> {
                            var genres = map.get(genre.getBookId());

                            if (genres == null) {
                                genres = new ArrayList<>();
                            }
                            genres.add(genre);
                            map.put(genre.getBookId(), genres);
                        }
                );
    }

    private Mono<Book> findBookById(Long bookId) {
        return genreRepository.findByBookId(bookId)
                .flatMap(genre -> Mono.just(genre).zipWith(bookRepository.findById(bookId)))
                .collect(Book::new, (book, tuple) -> {
                    if (book.getId() == 0) {
                        book.setId(tuple.getT2().getId());
                        book.setTitle(tuple.getT2().getTitle());
                    }

                    var genre = tuple.getT1();
                    if (book.getGenres() == null) {
                        book.setGenres(List.of(genre));
                    } else {
                        var newGenres = new ArrayList<Genre>();
                        newGenres.add(genre);
                        newGenres.addAll(book.getGenres());
                        book.setGenres(newGenres);
                    }
                })
                .zipWith(authorRepository.findByBookId(bookId))
                .map(tuple -> {
                    tuple.getT1().setAuthor(tuple.getT2());
                    return tuple.getT1();
                });
    }
}

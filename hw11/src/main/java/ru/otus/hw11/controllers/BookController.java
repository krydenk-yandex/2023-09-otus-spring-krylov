package ru.otus.hw11.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw11.dto.BookSaveDto;
import ru.otus.hw11.models.Author;
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

    @GetMapping("/api/books/{bookId}")
    public Mono<Book> getBookById(@PathVariable Long bookId) {
        return findBookById(bookId);
    }

    @GetMapping("/api/books")
    public Flux<Book> getBooks() {
        var booksWithAuthorIdAndGenresIds = bookRepository.findAllWithAuthorIdAndGenresIds().collectList();
        var genresByIdMap = genreRepository.findAll().collectMap(Genre::getId);
        var authorsByIdMap = authorRepository.findAll().collectMap(Author::getId);

        return Mono.zip(booksWithAuthorIdAndGenresIds, genresByIdMap, authorsByIdMap)
                .flatMapMany(tuple3 -> {
                    var booksWithRelationsIds = tuple3.getT1();
                    var genresMap = tuple3.getT2();
                    var authorsMap = tuple3.getT3();

                    return Flux.fromStream(
                            booksWithRelationsIds.stream().map(bookWithRelationsIds -> new Book(
                                bookWithRelationsIds.getId(),
                                bookWithRelationsIds.getTitle(),
                                authorsMap.get(bookWithRelationsIds.getAuthorId()),
                                bookWithRelationsIds.getGenresIds().stream().map(genresMap::get).toList()
                            ))
                    );
                });
    }

    @PutMapping("/api/books/{bookId}")
    public Mono<Book> editBook(
            @PathVariable Long bookId,
            BookSaveDto dto
    ) {
        Flux<Void> genreSaveFlux = Flux.fromIterable(dto.getGenresIds())
                .flatMap(genreId -> bookRepository.insertBookGenre(bookId, genreId));

        return bookRepository.deleteBookGenres(bookId)
                .thenMany(genreSaveFlux)
                .then(bookRepository.update(bookId, dto.getAuthorId(), dto.getTitle()))
                .then(findBookById(bookId));

    }

    @PostMapping("/api/books")
    public Mono<Book> insertBook(
            @Valid BookSaveDto dto
    ) {
        return bookRepository.insert(dto.getAuthorId(), dto.getTitle())
                .flatMap(bookId -> Flux.fromIterable(dto.getGenresIds())
                        .flatMap(genreId -> bookRepository.insertBookGenre(bookId, genreId))
                        .then(Mono.just(bookId)))
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

    private Mono<Book> findBookById(Long bookId) {
        return bookRepository.findByIdWithAuthorIdAndGenresIds(bookId)
                .flatMap(b -> Mono.zip(
                        Mono.just(b),
                        authorRepository.findById(b.getAuthorId()),
                        genreRepository.findAllById(b.getGenresIds()).collectList()
                ))
                .map(tuple3 -> new Book(
                        tuple3.getT1().getId(),
                        tuple3.getT1().getTitle(),
                        tuple3.getT2(),
                        tuple3.getT3()
                ));
    }
}

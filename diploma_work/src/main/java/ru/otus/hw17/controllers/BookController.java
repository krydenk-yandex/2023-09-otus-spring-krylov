package ru.otus.hw17.controllers;

import java.util.List;
import java.util.Optional;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw17.dto.BookDto;
import ru.otus.hw17.dto.BookSaveDto;
import ru.otus.hw17.exceptions.EntityNotFoundException;
import ru.otus.hw17.models.Book;
import ru.otus.hw17.services.BookService;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping("/api/books")
    public List<BookDto> booksList() {
        return bookService.findAll();
    }

    @GetMapping("/api/books/{bookId}")
    public Book getBookById(@PathVariable Long bookId) {
        Optional<Book> book = bookService.findById(bookId);

        if (book.isEmpty()) {
            throw new EntityNotFoundException("The book was not found");
        }

        return book.get();
    }

    @PutMapping("/api/books/{bookId}")
    public Book editBook(
            @PathVariable Long bookId,
            @Valid BookSaveDto dto
    ) {
        Optional<Book> book = bookService.findById(bookId);

        if (book.isEmpty()) {
            throw new EntityNotFoundException("The book was not found");
        }

        return bookService.update(bookId, dto.getTitle(), dto.getAuthorId(), dto.getGenresIds());
    }

    @PostMapping("/api/books")
    public Book createBook(@Valid BookSaveDto dto) {
        return bookService.insert(dto.getTitle(), dto.getAuthorId(), dto.getGenresIds());
    }

    @DeleteMapping("/api/books/{bookId}")
    public void deleteBook(@PathVariable Long bookId) {
        if (bookService.existById(bookId)) {
            bookService.deleteById(bookId);
        }
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleNotFound(EntityNotFoundException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}

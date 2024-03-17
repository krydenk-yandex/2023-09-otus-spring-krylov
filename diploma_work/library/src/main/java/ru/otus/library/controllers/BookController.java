package ru.otus.library.controllers;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.library.dto.BookDto;
import ru.otus.library.dto.BookSaveDto;
import ru.otus.library.dto.BookWithChaptersDto;
import ru.otus.library.exceptions.EntityNotFoundException;
import ru.otus.library.services.BookService;
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
    public BookWithChaptersDto getBookById(@PathVariable Long bookId) {
        var book = bookService.findById(bookId);

        if (book.isEmpty()) {
            throw new EntityNotFoundException("The book was not found");
        }

        return book.get();
    }

    @PutMapping("/api/books/{bookId}")
    public BookWithChaptersDto editBook(
            @PathVariable Long bookId,
            @Valid @RequestBody BookSaveDto dto
    ) {
        var book = bookService.findById(bookId);

        if (book.isEmpty()) {
            throw new EntityNotFoundException("The book was not found");
        }

        return bookService.update(
                bookId,
                dto.getTitle(),
                dto.getAuthorId(),
                dto.getGenresIds(),
                dto.getChapters()
        );
    }

    @PostMapping("/api/books")
    public BookWithChaptersDto createBook(@Valid @RequestBody BookSaveDto dto) {
        return bookService.insert(dto.getTitle(), dto.getAuthorId(), dto.getGenresIds(), dto.getChapters());
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
        return ResponseEntity.badRequest().body(ex.getFieldErrors()
                .stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)));
    }
}

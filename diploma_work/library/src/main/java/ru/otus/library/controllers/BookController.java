package ru.otus.library.controllers;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.otus.library.dto.BookDto;
import ru.otus.library.dto.BookSaveDto;
import ru.otus.library.dto.BookWithChaptersDto;
import ru.otus.library.exceptions.EntityNotFoundException;
import ru.otus.library.services.BookService;
import ru.otus.library.services.FileService;
import ru.otus.library.utils.FileUtils;

@RestController
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    private final FileService fileService;

    private final String BOOK_COVER_NAME = "book_cover";

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

        var fileUrl = book.get().getCoverUrl();
        if (dto.getCoverBase64() != null) {
            fileUrl = fileService.putFileToFileStoreAndReturnUrl(
                    (new FileUtils()).convertFromBase64(BOOK_COVER_NAME, dto.getCoverBase64())
            );
        }

        return bookService.update(
                bookId,
                dto.getTitle(),
                fileUrl,
                dto.getAuthorId(),
                dto.getGenresIds(),
                dto.getChapters()
        );
    }

    @PostMapping("/api/books")
    public BookWithChaptersDto createBook(
            @Valid @RequestBody BookSaveDto dto
    ) {
        if (dto.getCoverBase64() == null) {
            throw new IllegalArgumentException("Обложка не должна быть пустой");
        }
        var fileUrl = fileService.putFileToFileStoreAndReturnUrl(
                (new FileUtils()).convertFromBase64(BOOK_COVER_NAME, dto.getCoverBase64())
        );

        return bookService.insert(
                dto.getTitle(),
                fileUrl,
                dto.getAuthorId(),
                dto.getGenresIds(),
                dto.getChapters()
        );
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

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
    }
}

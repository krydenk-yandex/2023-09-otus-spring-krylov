package ru.otus.hw12.controllers;

import java.util.List;
import java.util.Optional;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.hw12.converters.BookConverter;
import ru.otus.hw12.dto.BookDto;
import ru.otus.hw12.dto.BookSaveDto;
import ru.otus.hw12.exceptions.EntityNotFoundException;
import ru.otus.hw12.models.Book;
import ru.otus.hw12.services.AuthorService;
import ru.otus.hw12.services.BookService;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.hw12.services.GenreService;

@Controller
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    private final GenreService genreService;

    private final AuthorService authorService;

    private final BookConverter bookConverter;

    @GetMapping("/")
    public String index() {
        return "redirect:/books";
    }

    @GetMapping("/books")
    public String booksList(Model model) {
        List<BookDto> books = bookService.findAll();
        model.addAttribute("books", books);

        return "booksList";
    }

    @GetMapping("/books/edit/{bookId}")
    public String editBookPage(@PathVariable Long bookId, Model model) {
        Optional<Book> book = bookService.findById(bookId);

        if (book.isEmpty()) {
            throw new EntityNotFoundException("The book was not found");
        }

        model.addAttribute("bookId", book.get().getId());
        model.addAttribute("book", bookConverter.toSaveDto(book.get()));
        model.addAttribute("genres", genreService.findAll());
        model.addAttribute("authors", authorService.findAll());

        return "bookEdit";
    }

    @PostMapping("/books/edit/{bookId}")
    public String editBook(
            @PathVariable @ModelAttribute("bookId") Long bookId,
            @Valid @ModelAttribute("book") BookSaveDto dto,
            BindingResult bindingResult,
            Model model
    ) {
        Optional<Book> book = bookService.findById(bookId);

        if (book.isEmpty()) {
            throw new EntityNotFoundException("The book was not found");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("genres", genreService.findAll());
            model.addAttribute("authors", authorService.findAll());
            return "bookEdit";
        }

        bookService.update(bookId, dto.getTitle(), dto.getAuthorId(), dto.getGenresIds());

        return "redirect:/books";
    }

    @GetMapping("/books/create")
    public String createBookPage(Model model) {
        model.addAttribute("genres", genreService.findAll());
        model.addAttribute("authors", authorService.findAll());
        model.addAttribute("book", new BookSaveDto());

        return "bookCreate";
    }

    @PostMapping("/books/create")
    public String createBook(
            @Valid @ModelAttribute("book") BookSaveDto dto,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("genres", genreService.findAll());
            model.addAttribute("authors", authorService.findAll());
            return "bookCreate";
        }

        bookService.insert(dto.getTitle(), dto.getAuthorId(), dto.getGenresIds());

        return "redirect:/books";
    }

    @PostMapping("/books/delete/{bookId}")
    public String deleteBook(@PathVariable Long bookId) {
        if (bookService.existById(bookId)) {
            bookService.deleteById(bookId);
        }

        return "redirect:/books";
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleNotFound(EntityNotFoundException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}

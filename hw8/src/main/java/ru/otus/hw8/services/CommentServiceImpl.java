package ru.otus.hw8.services;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw8.exceptions.EntityNotFoundException;
import ru.otus.hw8.models.Comment;
import ru.otus.hw8.repositories.BookRepository;
import ru.otus.hw8.repositories.CommentRepository;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<Comment> findById(String id) {
        return commentRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comment> findByBookId(String bookId) {
        return commentRepository.findByBookId(bookId);
    }

    @Override
    @Transactional
    public Comment insert(String text, String authorName, String bookId) {
        return save(null, text, authorName, bookId);
    }

    @Override
    @Transactional
    public Comment update(String id, String text, String authorName, String bookId) {
        return save(id, text, authorName, bookId);
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        commentRepository.deleteById(id);
    }

    private Comment save(String id, String text, String authorName, String bookId) {
        var book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %s not found".formatted(bookId)));

        var comment = new Comment(id, text, authorName, book);
        return commentRepository.save(comment);
    }
}

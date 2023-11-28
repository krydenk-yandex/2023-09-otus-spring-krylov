package ru.otus.hw7.services;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw7.models.Comment;
import ru.otus.hw7.repositories.CommentRepository;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    @Override
    public Optional<Comment> findById(long id) {
        return commentRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comment> findByBookId(long bookId) {
        return commentRepository.findByBookId(bookId);
    }
}

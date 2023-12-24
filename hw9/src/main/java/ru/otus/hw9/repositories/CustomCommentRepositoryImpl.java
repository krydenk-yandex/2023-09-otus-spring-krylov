package ru.otus.hw9.repositories;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import ru.otus.hw9.exceptions.EntityNotFoundException;
import ru.otus.hw9.models.Book;
import ru.otus.hw9.models.Comment;

@RequiredArgsConstructor
public class CustomCommentRepositoryImpl implements CustomCommentRepository {
    @PersistenceContext
    private final EntityManager em;

    @Override
    public List<Comment> findByBookId(long bookId) {
        return Optional.ofNullable(em.find(Book.class, bookId))
                .map(Book::getId)
                .map(id -> em.createQuery("select c from Comment c where c.book.id = :book_id", Comment.class)
                        .setParameter("book_id", id)
                        .getResultList())
                .orElseThrow(() -> new EntityNotFoundException("Book for load comments not found"));
    }
}

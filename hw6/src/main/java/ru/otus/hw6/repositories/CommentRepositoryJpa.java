package ru.otus.hw6.repositories;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw6.exceptions.EntityNotFoundException;
import ru.otus.hw6.models.Comment;
import ru.otus.hw6.models.Book;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryJpa implements CommentRepository {

    @PersistenceContext
    private final EntityManager em;

    public List<Comment> findByBookId(long bookId) {
        return Optional.ofNullable(em.find(Book.class, bookId))
                .map(Book::getId)
                .map(id -> em.createQuery("select c from Comment c where c.book.id = :book_id", Comment.class)
                        .setParameter("book_id", id)
                        .getResultList())
                .orElseThrow(() -> new EntityNotFoundException("Book for load comments not found"));
    }

    public Optional<Comment> findById(long id) {
        return Optional.ofNullable(em.find(Comment.class, id));
    }
}

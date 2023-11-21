package ru.otus.hw6.repositories;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw6.models.Comment;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryJpa implements CommentRepository {

    @PersistenceContext
    private final EntityManager em;

    public List<Comment> findByBookId(long bookId) {
        return em.createQuery(
            """
                select c from Comment c
                join fetch c.book
                join fetch c.book.author
                where c.book.id = :book_id
            """,
            Comment.class
        )
        .setParameter("book_id", bookId)
        .getResultList();
    }

    public Optional<Comment> findById(long id) {
        return Optional.ofNullable(em.find(Comment.class, id));
    }
}

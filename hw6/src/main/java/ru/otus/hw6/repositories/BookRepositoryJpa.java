package ru.otus.hw6.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Repository;
import ru.otus.hw6.models.Book;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BookRepositoryJpa implements BookRepository {

    @PersistenceContext
    private final EntityManager em;

    @Override
    public Optional<Book> findById(long id) {
        Map<String, Object> properties = new HashMap<>();
        properties.put(
                "jakarta.persistence.fetchgraph",
                em.getEntityGraph("book-with-author-and-genres"));

        return Optional.ofNullable(em.find(Book.class, id, properties));
    }

    @Override
    public List<Book> findAll() {
        return withGenres(
                em.createQuery("select b from Book b", Book.class)
                        .setHint(
                                "jakarta.persistence.fetchgraph",
                                em.getEntityGraph("book-with-author")
                        ).getResultList()
        );
    }

    private List<Book> withGenres(List<Book> books) {
        if (books.size() > 0) {
            // getting genres for the whole collection by subselect
            Hibernate.initialize(books.get(0).getGenres());
        }

        return books;
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            em.persist(book);
            return book;
        }
        return em.merge(book);
    }

    @Override
    public void deleteById(long id) {
        var book = em.find(Book.class, id);

        if (book == null) {
            return;
        }

        em.remove(book);
    }
}

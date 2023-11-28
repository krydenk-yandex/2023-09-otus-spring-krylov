package ru.otus.hw7.repositories;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import ru.otus.hw7.models.Book;

@RequiredArgsConstructor
public class CustomBookRepositoryImpl implements CustomBookRepository {
    @PersistenceContext
    private final EntityManager em;

    @Override
    public List<Book> findAllWithAuthorAndGenres() {
        TypedQuery<Book> query = em.createQuery("select b from Book b", Book.class)
                .setHint(
                        "jakarta.persistence.fetchgraph",
                        em.getEntityGraph("book-with-author")
                );

        return withGenres(query.getResultList());
    }

    private List<Book> withGenres(List<Book> books) {
        if (books.size() > 0) {
            // getting genres for the whole collection by subselect
            books.get(0).getGenres().size();
        }

        return books;
    }
}

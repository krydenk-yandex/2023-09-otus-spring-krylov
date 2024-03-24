package ru.otus.library.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.otus.library.models.Bookmark;
import ru.otus.library.models.Chapter;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    List<Bookmark> findByTypeAndUserId(Bookmark.BookmarkType type, Long userId);

    @Query("""
        select b from Bookmark b
        join b.chapter c
        where b.type = ?1 and c.book.id = ?2 and b.userId = ?3
    """)
    List<Bookmark> findBookmarksByBookIdAndType(Bookmark.BookmarkType type, Long bookId, Long userId);

    @Query("select c from Bookmark b " +
            "join b.chapter c " +
            "where b.type = 'AUTO' and c.book.id = :bookId " +
            "order by c.orderNumber DESC")
    Optional<Chapter> findLatestAutoBookmarkChapterByBookId(Long bookId);

    @Query("select c from Bookmark b " +
            "join b.chapter c " +
            "where b.type = 'AUTO' and c.book.id = :bookId and b.userId = :userId " +
            "order by c.orderNumber DESC")
    Optional<Chapter> findLatestAutoBookmarkChapterByBookIdAndUserId(Long bookId, Long userId);
}
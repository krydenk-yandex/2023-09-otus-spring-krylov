package ru.otus.library.services;

import ru.otus.library.models.Bookmark;
import ru.otus.library.models.Chapter;

import java.util.List;
import java.util.Optional;

public interface BookmarkService {
    List<Bookmark> getAllManualByUserId(Long userId);

    List<Bookmark> getAllManualByBookIdAndUserId(Long bookId, Long userId);

    Optional<Chapter> findLatestAutoBookmarkChapterByBookId(Long bookId, Long userId);
}

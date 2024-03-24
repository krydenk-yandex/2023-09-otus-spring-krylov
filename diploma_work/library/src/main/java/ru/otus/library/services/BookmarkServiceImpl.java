package ru.otus.library.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.library.models.Bookmark;
import ru.otus.library.models.Chapter;
import ru.otus.library.repositories.BookmarkRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookmarkServiceImpl implements BookmarkService {
    private BookmarkRepository bookmarkRepository;

    @Override
    public List<Bookmark> getAllManualByUserId(Long userId) {
        return bookmarkRepository.findByTypeAndUserId(Bookmark.BookmarkType.MANUAL, userId);
    }

    @Override
    public List<Bookmark> getAllManualByBookIdAndUserId(Long bookId, Long userId) {
        return bookmarkRepository.findBookmarksByBookIdAndType(Bookmark.BookmarkType.MANUAL,
                bookId,
                userId);
    }

    @Override
    public Optional<Chapter> findLatestAutoBookmarkChapterByBookId(Long bookId, Long userId) {
        return bookmarkRepository.findLatestAutoBookmarkChapterByBookIdAndUserId(bookId, userId);
    }
}

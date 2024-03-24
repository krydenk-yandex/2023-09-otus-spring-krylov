package ru.otus.library.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.library.models.Bookmark;
import ru.otus.library.models.Chapter;
import ru.otus.library.services.BookmarkService;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/bookmarks")
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @GetMapping("/manual/user/{userId}")
    public ResponseEntity<List<Bookmark>> getAllManualByUserId(@PathVariable Long userId) {
        List<Bookmark> bookmarks = bookmarkService.getAllManualByUserId(userId);
        return ResponseEntity.ok(bookmarks);
    }

    @GetMapping("/manual/book/{bookId}/user/{userId}")
    public ResponseEntity<List<Bookmark>> getAllManualByBookIdAndUserId(@PathVariable Long bookId,
                                                                        @PathVariable Long userId) {
        List<Bookmark> bookmarks = bookmarkService.getAllManualByBookIdAndUserId(bookId, userId);
        return ResponseEntity.ok(bookmarks);
    }

    @GetMapping("/auto/latest/book/{bookId}/user/{userId}")
    public ResponseEntity<Chapter> findLatestAutoBookmarkChapterByBookId(@PathVariable Long bookId,
                                                                         @PathVariable Long userId) {
        Optional<Chapter> chapter = bookmarkService.findLatestAutoBookmarkChapterByBookId(bookId, userId);
        return chapter.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
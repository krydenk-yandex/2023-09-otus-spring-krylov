package ru.otus.library.converters;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.library.dto.BookReducedDto;
import ru.otus.library.dto.ChapterDto;
import ru.otus.library.dto.ChapterReducedDto;
import ru.otus.library.dto.ChapterSaveDto;
import ru.otus.library.models.Book;
import ru.otus.library.models.Chapter;

@Component
@AllArgsConstructor
public class ChapterConverter {
    public ChapterReducedDto toReducedDto(Chapter chapter) {
        return new ChapterReducedDto(
                chapter.getUuid(),
                chapter.getTitle()
        );
    }

    public ChapterDto toDto(Chapter chapter, BookReducedDto bookReducedDto) {
        return new ChapterDto(
                chapter.getUuid(),
                chapter.getTitle(),
                chapter.getText(),
                bookReducedDto,
                chapter.getNextChapter() != null
                    ? toReducedDto(chapter.getNextChapter())
                    : null,
                chapter.getPrevChapter() != null
                    ? toReducedDto(chapter.getPrevChapter())
                    : null
        );
    }

    public List<Chapter> toEntities(List<ChapterSaveDto> chapterDtos, Book book) {
        AtomicReference<Integer> order = new AtomicReference<>(0);

        var chapters = chapterDtos.stream().map(dto -> {
            final UUID uuid;
            final boolean isNew;

            if (dto.getUuid() == null) {
                uuid = UUID.randomUUID();
                isNew = true;
            } else {
                uuid = dto.getUuid();
                isNew = false;
            }

            var chapter = new Chapter(
                    uuid,
                    order.getAndSet(order.get() + 1),
                    dto.getTitle(),
                    dto.getText(),
                    book,
                    null,
                    null
            );

            chapter.setIsNew(isNew);

            return chapter;
        }).toList();

        var it = chapters.iterator();
        Chapter cur = it.next();
        Chapter prev = null;
        while (it.hasNext()) {
            cur.setPrevChapter(prev);
            var next = it.next();
            cur.setNextChapter(next);
            prev = cur;
            cur = next;
        }
        cur.setPrevChapter(prev);

        return chapters;
    }
}

package ru.otus.library.converters;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.library.dto.ChapterDto;
import ru.otus.library.dto.ChapterReducedDto;
import ru.otus.library.models.Chapter;

@Component
@AllArgsConstructor
public class ChapterConverter {
    private BookConverter bookConverter;

    public ChapterReducedDto toReducedDto(Chapter chapter) {
        return new ChapterReducedDto(
                chapter.getUuid(),
                chapter.getTitle()
        );
    }

    public ChapterDto toDto(Chapter chapter) {
        return new ChapterDto(
                chapter.getUuid(),
                chapter.getTitle(),
                chapter.getText(),
                bookConverter.toReducedDto(chapter.getBook()),
                chapter.getNextChapter() != null
                    ? toReducedDto(chapter.getNextChapter())
                    : null,
                chapter.getPrevChapter() != null
                    ? toReducedDto(chapter.getPrevChapter())
                    : null
        );
    }
}

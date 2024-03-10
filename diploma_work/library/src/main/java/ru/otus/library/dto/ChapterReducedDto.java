package ru.otus.library.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ChapterReducedDto {
    private UUID uuid;

    private String title;
}

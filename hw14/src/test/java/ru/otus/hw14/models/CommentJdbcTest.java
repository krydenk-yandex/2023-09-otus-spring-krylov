package ru.otus.hw14.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentJdbcTest {
    private long id;

    private String text;

    private String authorName;

    private String bookTitle;
}

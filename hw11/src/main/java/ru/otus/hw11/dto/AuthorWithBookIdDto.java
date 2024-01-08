package ru.otus.hw11.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthorWithBookIdDto {
    private long id;

    private String fullName;

    private long bookId;
}

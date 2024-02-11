package ru.otus.hw14.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthorJdbc {
    private long id;

    private String fullName;
}

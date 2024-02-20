package ru.otus.hw15.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Notification {
    private String userName;

    private String message;
}

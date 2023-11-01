package ru.otus.hw4.service;

public interface LocalizedMessagesService {
    String getMessage(String code, Object ...args);
}

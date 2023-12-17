package ru.otus.hw8.exceptions;

public class EntityDeletionForbidException extends RuntimeException {
    public EntityDeletionForbidException(String message) {
        super(message);
    }
}

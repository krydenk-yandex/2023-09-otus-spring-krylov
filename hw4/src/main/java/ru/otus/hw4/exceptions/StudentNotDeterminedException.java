package ru.otus.hw4.exceptions;

public class StudentNotDeterminedException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Test was ran before putting student name and surname";

    public StudentNotDeterminedException(String message, Throwable ex) {
        super(message, ex);
    }

    public StudentNotDeterminedException(Throwable ex) {
        super(DEFAULT_MESSAGE, ex);
    }

    public StudentNotDeterminedException() {
        super(DEFAULT_MESSAGE);
    }
}

package ru.otus.spring.exception;

public class TasksCsvReadException extends RuntimeException {
    public TasksCsvReadException(Throwable e) {
        super(e);
    }

    @Override
    public String getMessage() {
        return "Error on attempt to read from tasks CSV resource" + (super.getMessage() != null ? ": " + super.getMessage() : ".");
    }
}

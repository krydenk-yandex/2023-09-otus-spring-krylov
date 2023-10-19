package ru.otus.spring.exception;

public class TasksCsvMalformedException extends RuntimeException {
    @Override
    public String getMessage() {
        return "CSV file is malformed: " + super.getMessage();
    }
}

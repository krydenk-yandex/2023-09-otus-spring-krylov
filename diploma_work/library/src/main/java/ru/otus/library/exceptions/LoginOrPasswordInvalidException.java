package ru.otus.library.exceptions;

public class LoginOrPasswordInvalidException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Введена некорректная пара логин-пароль";

    public LoginOrPasswordInvalidException() {
        super(DEFAULT_MESSAGE);
    }
}

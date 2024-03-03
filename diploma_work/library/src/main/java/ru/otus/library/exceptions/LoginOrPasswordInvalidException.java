package ru.otus.library.exceptions;

import javax.xml.bind.annotation.XmlType;

public class LoginOrPasswordInvalidException extends RuntimeException {
    private final static String DEFAULT_MESSAGE = "Введена некорректная пара логин-пароль";

    public LoginOrPasswordInvalidException() {
        super(DEFAULT_MESSAGE);
    }
}

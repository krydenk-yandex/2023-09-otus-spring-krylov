package ru.otus.library.exceptions;

import java.util.List;

public class InvalidFileExtensionException extends RuntimeException {
    public InvalidFileExtensionException() {
        super("Неверный формат файла");
    }

    public InvalidFileExtensionException(List<String> allowedExtensions) {
        super("Неверный формат файла, доступные форматы: "
                + String.join(", ", allowedExtensions));
    }

    public InvalidFileExtensionException(String message) {
        super(message);
    }
}

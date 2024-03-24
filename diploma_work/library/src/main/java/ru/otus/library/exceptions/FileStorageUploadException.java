package ru.otus.library.exceptions;

public class FileStorageUploadException extends RuntimeException {
    public FileStorageUploadException(Throwable cause) {
        super("Ошибка при загрузке файла во внешнее хранилище", null);
    }

    public FileStorageUploadException() {
        super("Ошибка при загрузке файла во внешнее хранилище");
    }
}

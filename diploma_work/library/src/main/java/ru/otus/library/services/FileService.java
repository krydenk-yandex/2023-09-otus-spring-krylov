package ru.otus.library.services;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    String putFileToFileStoreAndReturnUrl(MultipartFile file);

    boolean isExtensionValid(MultipartFile file);
}

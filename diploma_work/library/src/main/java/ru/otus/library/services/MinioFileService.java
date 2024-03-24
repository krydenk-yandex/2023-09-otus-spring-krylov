package ru.otus.library.services;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.otus.library.exceptions.FileStorageUploadException;
import ru.otus.library.exceptions.InvalidFileExtensionException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MinioFileService implements FileService {

    private final MinioClient minioClient;

    @Value("${minio.bucket}")
    @Setter
    private String bucketName;

    @Value("#{'${file.allowed-extensions}'.split(', ')}")
    @Setter
    private List<String> allowedExtensions;

    private String getFileName(MultipartFile file) {
        return String.join("_",
                String.valueOf(System.currentTimeMillis()),
                String.valueOf(Long.valueOf((long)(Math.random() * 1000))),
                file.getOriginalFilename()
        );
    }

    public String putFileToFileStoreAndReturnUrl(MultipartFile file) {
        if (!isExtensionValid(file)) {
            throw new InvalidFileExtensionException(allowedExtensions);
        }

        try {
            String fileName = getFileName(file);

            minioClient.putObject(
                    PutObjectArgs.builder().bucket(bucketName).object(fileName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build());

            return "/" + fileName;
        } catch (IOException | ErrorResponseException | InsufficientDataException | InternalException |
                 InvalidKeyException | InvalidResponseException | NoSuchAlgorithmException | ServerException |
                 XmlParserException e) {
            throw new FileStorageUploadException(e);
        }
    }

    public boolean isExtensionValid(MultipartFile file) {
        for (String ext : allowedExtensions) {
            if (Objects.requireNonNull(file.getOriginalFilename()).toLowerCase().endsWith(ext)) {
                return true;
            }
        }
        return false;
    }
}

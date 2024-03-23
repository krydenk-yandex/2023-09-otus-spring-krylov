package ru.otus.library.utils;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class FileUtils {
    private static final Map<String, String> mimeTypeToExtensionMap;

    static {
        mimeTypeToExtensionMap = new HashMap<>();
        mimeTypeToExtensionMap.put("application/javascript", ".js");
        mimeTypeToExtensionMap.put("text/html", ".html");
        mimeTypeToExtensionMap.put("text/plain", ".txt");
        mimeTypeToExtensionMap.put("image/jpeg", ".jpg");
        mimeTypeToExtensionMap.put("image/png", ".png");
        mimeTypeToExtensionMap.put("application/pdf", ".pdf");
    }

    public MultipartFile convertFromBase64(String fileName, String base64String) {
        // Ensure the data URL starts with "data:"
        if (!base64String.startsWith("data:")) {
            throw new IllegalArgumentException("Invalid base64 string.");
        }

        // Remove the "data:" prefix and split into MIME type and base64 data
        String[] parts = base64String.substring(5).split(";base64,");

        var mimeType = parts[0];
        var base64Data = parts[1];

        var extension = mimeTypeToExtensionMap.getOrDefault(mimeType, "");

        byte[] fileContent = Base64.getDecoder().decode(base64Data);

        return new CustomMultipartFile(
                fileContent,
                fileName + extension,
                mimeType);
    }

    public static class CustomMultipartFile implements MultipartFile {

        private final byte[] fileContent;
        private final String fileName;
        private final String contentType;

        public CustomMultipartFile(byte[] fileContent, String fileName, String contentType) {
            this.fileContent = fileContent;
            this.fileName = fileName;
            this.contentType = contentType;
        }

        @Override
        public String getName() {
            return null;
        }

        @Override
        public String getOriginalFilename() {
            return fileName;
        }

        @Override
        public String getContentType() {
            return contentType;
        }

        @Override
        public boolean isEmpty() {
            return (fileContent == null || fileContent.length == 0);
        }

        @Override
        public long getSize() {
            return fileContent.length;
        }

        @Override
        public byte[] getBytes() throws IOException {
            return fileContent;
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream(fileContent);
        }

        @Override
        public void transferTo(File dest) throws IOException, IllegalStateException {
            try (OutputStream out = new FileOutputStream(dest)) {
                out.write(fileContent);
            }
        }

        @Override
        public Resource getResource() {
            return new ByteArrayResource(fileContent);
        }

        @Override
        public void transferTo(Path dest) throws IOException, IllegalStateException {
            Files.write(dest, fileContent);
        }
    }
}

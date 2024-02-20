package ru.otus.hw15.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BuildArtifact {
    private SourceCodeRepository source;

    private String fileName;
}

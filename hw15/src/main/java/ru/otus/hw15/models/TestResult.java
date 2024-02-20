package ru.otus.hw15.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TestResult {
    private boolean passed;

    private BuildArtifact artifact;
}

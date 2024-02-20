package ru.otus.hw15.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DeployResult {
    private boolean success;

    private BuildArtifact artifact;

    private String output;
}

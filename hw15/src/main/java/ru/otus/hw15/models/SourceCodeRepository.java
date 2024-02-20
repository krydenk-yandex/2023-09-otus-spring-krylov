package ru.otus.hw15.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SourceCodeRepository {
    private String url;

    private List<String> owners;
}

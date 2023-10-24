package ru.otus.hw3.converter;

import ru.otus.hw3.domain.Question;

public interface QuestionToStringConverter {
    String convertToString(Question question);
}

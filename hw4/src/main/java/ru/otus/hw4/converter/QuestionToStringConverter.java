package ru.otus.hw4.converter;

import ru.otus.hw4.domain.Question;

public interface QuestionToStringConverter {
    String convertToString(Question question);
}

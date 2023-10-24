package ru.otus.hw.converter;

import ru.otus.hw.domain.Question;

public interface QuestionToStringConverter {
    String convertToString(Question question);
}

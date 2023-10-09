package ru.otus.spring.converter;

import ru.otus.spring.domain.TestTask;

public interface TestTaskToStringConverter {
    String convertToString(TestTask task);
}

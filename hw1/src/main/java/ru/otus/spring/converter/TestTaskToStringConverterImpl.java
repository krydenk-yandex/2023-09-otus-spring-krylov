package ru.otus.spring.converter;

import ru.otus.spring.domain.TestTask;

public class TestTaskToStringConverterImpl implements TestTaskToStringConverter {
    @Override
    public String convertToString(TestTask task) {
        return "Question: '" + task.getQuestion() + "', answer: '" + task.getAnswer() + "'";
    }
}
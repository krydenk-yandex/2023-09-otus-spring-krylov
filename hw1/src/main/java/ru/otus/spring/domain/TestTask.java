package ru.otus.spring.domain;

public class TestTask {

    private final String answer;

    private final String question;

    public TestTask(String question, String answer) {
        this.answer = answer;
        this.question = question;
    }

    @Override
    public String toString() {
        return "Question: '" + question + "', answer: '" + answer + "'";
    }
}
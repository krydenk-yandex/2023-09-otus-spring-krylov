package ru.otus.spring.domain;

public class TestTask {

    private final String question;

    private final String answer;

    public TestTask(String question, String answer) {
        this.answer = answer;
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public String getQuestion() {
        return question;
    }
}
package ru.otus.spring.domain;

public class TestTask {

    private final String answer;

    private final String question;

    public String getAnswer() {
        return answer;
    }

    public String getQuestion() {
        return question;
    }

    public TestTask(String question, String answer) {
        this.answer = answer;
        this.question = question;
    }
}
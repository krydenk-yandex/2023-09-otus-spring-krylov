package ru.otus.hw4.service;

import ru.otus.hw4.domain.Student;

public interface TestRunnerService {
    Student getStudent();

    Student askStudentForNameAndSurname();

    void cleanStudent();

    void run();
}

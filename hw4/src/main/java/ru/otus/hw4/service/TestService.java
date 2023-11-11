package ru.otus.hw4.service;

import ru.otus.hw4.domain.Student;
import ru.otus.hw4.domain.TestResult;

public interface TestService {
    TestResult executeTestFor(Student student);
}

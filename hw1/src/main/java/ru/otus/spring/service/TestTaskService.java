package ru.otus.spring.service;

import java.util.Collection;

import ru.otus.spring.domain.TestTask;

public interface TestTaskService {
    Collection<TestTask> getTasks();
}
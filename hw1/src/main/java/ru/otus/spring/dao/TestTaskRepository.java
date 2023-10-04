package ru.otus.spring.dao;

import java.util.Collection;

import ru.otus.spring.domain.TestTask;

public interface TestTaskRepository {
    Collection<TestTask> getTasks();
}
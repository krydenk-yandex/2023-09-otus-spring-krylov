package ru.otus.spring.service;

import java.util.Collection;

import ru.otus.spring.dao.TestTaskRepository;
import ru.otus.spring.domain.TestTask;

public class TestTaskService {
    private final TestTaskRepository tasksRepository;

    public TestTaskService(TestTaskRepository tasksRepository) {
        this.tasksRepository = tasksRepository;
    }

    public Collection<TestTask> getTasks() {
        return tasksRepository.getTasks();
    }
}
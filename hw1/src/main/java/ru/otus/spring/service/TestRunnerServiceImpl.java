package ru.otus.spring.service;

import ru.otus.spring.converter.TestTaskToStringConverter;

public class TestRunnerServiceImpl implements TestRunnerService {
    private final TestTaskService testTaskService;
    private final IOService ioService;
    private final TestTaskToStringConverter converter;

    public TestRunnerServiceImpl(
        TestTaskService testTaskService,
        IOService ioService,
        TestTaskToStringConverter converter
    ) {
        this.testTaskService = testTaskService;
        this.ioService = ioService;
        this.converter = converter;
    }

    @Override
    public void run() {
        var tasks = testTaskService.getTasks();
        tasks.forEach(t -> ioService.printLine(converter.convertToString(t)));
    }
}
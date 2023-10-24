package ru.otus.hw3.runner;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import ru.otus.hw3.service.ResultService;
import ru.otus.hw3.service.StudentService;
import ru.otus.hw3.service.TestService;

@Service
@RequiredArgsConstructor
public class TestRunner implements CommandLineRunner {

    private final TestService testService;

    private final StudentService studentService;

    private final ResultService resultService;

    @Override
    public void run(String... args) {
        var student = studentService.determineCurrentStudent();
        var testResult = testService.executeTestFor(student);
        resultService.showResult(testResult);
    }
}

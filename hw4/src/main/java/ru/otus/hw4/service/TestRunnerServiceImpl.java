package ru.otus.hw4.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw4.domain.Student;
import ru.otus.hw4.exceptions.StudentNotDeterminedException;

@Service
@RequiredArgsConstructor
public class TestRunnerServiceImpl implements TestRunnerService {

    private final TestService testService;

    private final StudentService studentService;

    private final ResultService resultService;

    private Student student;

    @Override
    public Student getStudent() {
        return student;
    }

    @Override
    public Student askStudentForNameAndSurname() {
        student = studentService.determineCurrentStudent();
        return student;
    }

    @Override
    public void cleanStudent() {
        student = null;
    }

    @Override
    public void run() {
        if (student == null) {
            throw new StudentNotDeterminedException();
        }
        var testResult = testService.executeTestFor(student);
        resultService.showResult(testResult);
    }
}

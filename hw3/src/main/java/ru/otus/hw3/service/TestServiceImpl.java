package ru.otus.hw3.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw3.converter.QuestionToStringConverter;
import ru.otus.hw3.dao.QuestionDao;
import ru.otus.hw3.domain.Student;
import ru.otus.hw3.domain.TestResult;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {


    private final LocalizedIOService ioService;

    private final QuestionDao questionDao;

    private final QuestionToStringConverter converter;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printLine("");
        ioService.printLineLocalized("TestService.answer.the.questions");
        var questions = questionDao.findAll();

        var testResult = new TestResult(student);

        for (var question: questions) {
            ioService.printLine("");
            ioService.printLine("-------------------------");
            ioService.printLine(converter.convertToString(question));

            var answerIndex = ioService.readIntForRangeLocalized(
                1,
                question.answers().size(),
                "TestService.wrong.answer"
            );

            testResult.applyAnswer(question, question.answers().get(answerIndex - 1).isCorrect());
        }
        return testResult;
    }
}

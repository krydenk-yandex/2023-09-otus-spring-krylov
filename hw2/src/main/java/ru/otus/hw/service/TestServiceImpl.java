package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.converter.QuestionToStringConverter;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    private final QuestionToStringConverter converter;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        var questions = questionDao.findAll();

        var testResult = new TestResult(student);

        for (var question: questions) {
            var answerIndex = ioService.readIntForRangeWithPrompt(
                1,
                question.answers().size(),
                converter.convertToString(question),
                "There is no answer with such an index, please provide a correct one"
            );

            testResult.applyAnswer(question, question.answers().get(answerIndex - 1).isCorrect());
        }
        return testResult;
    }
}

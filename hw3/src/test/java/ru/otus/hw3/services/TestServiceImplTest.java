package ru.otus.hw3.services;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.hw3.converter.QuestionToStringConverter;
import ru.otus.hw3.dao.QuestionDao;
import ru.otus.hw3.domain.Answer;
import ru.otus.hw3.domain.Question;
import ru.otus.hw3.domain.Student;
import ru.otus.hw3.service.LocalizedIOService;
import ru.otus.hw3.service.TestServiceImpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@DisplayName("Сервис проведения тестирования")
class TestServiceImplTest {
    @DisplayName("Должен возвращать результат с одним верным и одним неверным ответом")
    @Test
    void correctAndIncorrectAnswersInResult() {
        var questionDao = mock(QuestionDao.class);
        var ioService = mock(LocalizedIOService.class);
        var converter = mock(QuestionToStringConverter.class);

        var student = new Student("Ivan", "Petrov");

        var q1 = new Question("1 + 1", Arrays.asList(
                new Answer("1", false),
                new Answer("2", true)
        ));
        var q2 = new Question("1 + 2", Arrays.asList(
                new Answer("3", true),
                new Answer("1", false)
        ));
        List<Question> questionsList = Arrays.asList(q1, q2);

        given(questionDao.findAll()).willReturn(questionsList);

        given(ioService.readIntForRangeLocalized(anyInt(), anyInt(), anyString())).willReturn(1);

        var testService = new TestServiceImpl(ioService, questionDao, converter);
        var result = testService.executeTestFor(student);

        assertThat(result.getRightAnswersCount()).isEqualTo(1);
        assertThat(result.getAnsweredQuestions()).containsSequence(questionsList);
    }
}
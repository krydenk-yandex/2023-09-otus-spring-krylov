package ru.otus.services;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.hw.converter.QuestionToStringConverter;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.service.IOService;
import ru.otus.hw.service.TestServiceImpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@DisplayName("Сервис проведения тестирования")
class TestServiceImplTest {
    @DisplayName("Должен возвращать результат с одним верным и одним неверным ответом")
    @Test
    void correctAndIncorrectAnswersInResult() {
        var questionDao = mock(QuestionDao.class);
        var ioService = mock(IOService.class);
        var converter = mock(QuestionToStringConverter.class);

        var student = new Student("Ivan", "Petrov");

        var q1Text = "1 + 1";
        var q2Text = "1 + 2";
        var q1 = new Question(q1Text, Arrays.asList(
                new Answer("1", false),
                new Answer("2", true)
        ));
        var q2 = new Question(q2Text, Arrays.asList(
                new Answer("1", false),
                new Answer("3", true)
        ));
        List<Question> questionsList = Arrays.asList(q1, q2);

        given(questionDao.findAll()).willReturn(questionsList);

        given(converter.convertToString(q1)).willReturn(q1Text);
        given(converter.convertToString(q2)).willReturn(q2Text);

        given(ioService.readIntForRangeWithPrompt(anyInt(), anyInt(), eq(q1Text), anyString())).willReturn(1);
        given(ioService.readIntForRangeWithPrompt(anyInt(), anyInt(), eq(q2Text), anyString())).willReturn(2);

        given(questionDao.findAll()).willReturn(questionsList);

        var testService = new TestServiceImpl(ioService, questionDao, converter);
        var result = testService.executeTestFor(student);

        assertThat(result.getRightAnswersCount()).isEqualTo(1);
        assertThat(result.getAnsweredQuestions()).containsSequence(questionsList);
    }
}
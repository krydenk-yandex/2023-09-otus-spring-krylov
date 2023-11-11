package ru.otus.hw4.services;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw4.converter.QuestionToStringConverter;
import ru.otus.hw4.dao.QuestionDao;
import ru.otus.hw4.domain.Answer;
import ru.otus.hw4.domain.Question;
import ru.otus.hw4.domain.Student;
import ru.otus.hw4.service.LocalizedIOService;
import ru.otus.hw4.service.TestService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@DisplayName("Сервис проведения тестирования")
@SpringBootTest(properties = {
    "spring.shell.interactive.enabled=false",
    "spring.shell.script.enabled=false"
})
class TestServiceTest {
    @Autowired
    TestService testService;

    @MockBean
    QuestionDao questionDao;

    @MockBean
    LocalizedIOService ioService;

    @MockBean
    QuestionToStringConverter converter;

    Student student = new Student("Ivan", "Petrov");

    @BeforeEach
    void setUp() {
        given(ioService.readIntForRangeLocalized(anyInt(), anyInt(), anyString())).willReturn(1);
    }

    @DisplayName("Должен возвращать результат с одним верным и одним неверным ответом")
    @Test
    void correctAndIncorrectAnswersInResult() {
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

        var result = testService.executeTestFor(student);

        assertThat(result.getRightAnswersCount()).isEqualTo(1);
        assertThat(result.getAnsweredQuestions()).containsSequence(questionsList);
    }
}
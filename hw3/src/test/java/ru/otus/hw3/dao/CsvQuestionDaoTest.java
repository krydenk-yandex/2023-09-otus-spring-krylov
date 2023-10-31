package ru.otus.hw3.dao;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.hw3.config.TestFileNameProvider;
import ru.otus.hw3.domain.Answer;
import ru.otus.hw3.domain.Question;
import ru.otus.hw3.exceptions.QuestionReadException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@DisplayName("Читатель вопросов из CSV")
public class CsvQuestionDaoTest {

    @DisplayName("Должен вернуть корректный список из вопросов")
    @Test
    void readCorrectQuestionsFile() {
        var fileNameProvider = mock(TestFileNameProvider.class);
        given(fileNameProvider.getTestFileName()).willReturn("CsvQuestionDaoTest_correctFile.csv");

        var questionDao = new CsvQuestionDao(fileNameProvider);

        assertThat(questionDao.findAll()).containsSequence(Arrays.asList(
                new Question("1 + 1?", Arrays.asList(
                        new Answer("1", false),
                        new Answer("2", true)
                )),
                new Question("1 + 2?", Arrays.asList(
                        new Answer("1", false),
                        new Answer("3", true)
                ))
        ));
    }

    @DisplayName("Должно выброситься исключение, т.к. структура файла некорректная")
    @Test
    void readMalformedQuestionsFile() {
        var fileNameProvider = mock(TestFileNameProvider.class);
        given(fileNameProvider.getTestFileName()).willReturn("CsvQuestionDaoTest_malformedFile.csv");

        var questionDao = new CsvQuestionDao(fileNameProvider);

        assertThrows(QuestionReadException.class, questionDao::findAll);
    }
}

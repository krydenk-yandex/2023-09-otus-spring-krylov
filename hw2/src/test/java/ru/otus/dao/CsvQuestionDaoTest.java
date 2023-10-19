package ru.otus.dao;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.CsvQuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

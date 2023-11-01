package ru.otus.hw4.dao;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import ru.otus.hw4.config.TestFileNameProvider;
import ru.otus.hw4.domain.Answer;
import ru.otus.hw4.domain.Question;
import ru.otus.hw4.exceptions.QuestionReadException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@DisplayName("Читатель вопросов из CSV")
@SpringBootTest
@ContextConfiguration(classes = {CsvQuestionDao.class, TestFileNameProvider.class})
public class CsvQuestionDaoTest {
    @Autowired
    QuestionDao questionDao;

    @MockBean
    TestFileNameProvider fileNameProvider;

    @DisplayName("Должен вернуть корректный список из вопросов")
    @Test
    void readCorrectQuestionsFile() {
        given(fileNameProvider.getTestFileName()).willReturn("CsvQuestionDaoTest_correctFile.csv");
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
        given(fileNameProvider.getTestFileName()).willReturn("CsvQuestionDaoTest_malformedFile.csv");
        assertThrows(QuestionReadException.class, questionDao::findAll);
    }
}

package ru.otus.hw.converter;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.stereotype.Component;
import ru.otus.hw.domain.Question;

@Component
public class QuestionToStringConverterImpl implements QuestionToStringConverter {
    public String convertToString(Question question) {
        return question.text() + "\n\n" +
                IntStream.range(0, question.answers().size())
                        .mapToObj(i -> (i + 1) + ". " + question.answers().get(i).text())
                        .collect(Collectors.joining("\n"));
    }
}

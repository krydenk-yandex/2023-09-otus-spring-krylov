package ru.otus.hw4.dao;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;
import ru.otus.hw4.config.TestFileNameProvider;
import ru.otus.hw4.dao.dto.QuestionDto;
import ru.otus.hw4.domain.Question;
import ru.otus.hw4.exceptions.QuestionReadException;

@Repository
@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {
    private final TestFileNameProvider fileNameProvider;

    @Override
    public List<Question> findAll() {
        ClassPathResource resource = new ClassPathResource("/" + fileNameProvider.getTestFileName());
        try (var reader = new InputStreamReader(resource.getInputStream())) {
            List<QuestionDto> questionDtoList = new CsvToBeanBuilder<QuestionDto>(reader)
                .withType(QuestionDto.class)
                .withSeparator(';')
                .withSkipLines(1)
                .build().parse();
            return questionDtoList.stream().map(QuestionDto::toDomainObject).toList();
        } catch (IOException | RuntimeException e) {
            throw new QuestionReadException("Something went wrong on attempt to read questions CSV", e);
        }
    }
}

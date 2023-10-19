package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {
    private final TestFileNameProvider fileNameProvider;

    @Override
    public List<Question> findAll() {
        ClassPathResource resource = new ClassPathResource("/" + fileNameProvider.getTestFileName());
        try (var reader = new InputStreamReader(resource.getInputStream())) {
            List<QuestionDto> questionDtoList = new CsvToBeanBuilder(reader)
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

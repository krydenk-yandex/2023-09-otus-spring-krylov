package ru.otus.hw8.repositories;

import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import ru.otus.hw8.models.Genre;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Mongo Spring Data для работы с жанрами книги ")
@DataMongoTest
class GenreRepositoryTest {

    @Autowired
    private GenreRepository repository;

    @DisplayName("должен загружать список жанров по списку их id")
    @Test
    void shouldReturnCorrectGenresListByIds() {
        var actualGenres = repository.findByIdIn(List.of("1", "2", "3"));

        var expectedGenres = IntStream.range(1, 4).boxed()
                .map(id -> new Genre(
                        id.toString(),
                        "Genre_" + id
                ))
                .toList();

        assertThat(actualGenres)
            .usingRecursiveFieldByFieldElementComparator()
            .containsExactlyElementsOf(expectedGenres);
    }
}
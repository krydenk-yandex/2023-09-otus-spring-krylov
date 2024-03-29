package ru.otus.hw13.repositories;

import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw13.models.Genre;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Spring Data для работы с жанрами книги ")
@DataJpaTest
class GenreRepositoryTest {

    @Autowired
    private GenreRepository repository;

    @Autowired
    private TestEntityManager em;

    @DisplayName("должен загружать список жанров по списку их id")
    @Test
    void shouldReturnCorrectGenresListByIds() {
        var actualGenres = repository.findByIdIn(List.of(1L, 2L, 3L));

        actualGenres.forEach(genre -> em.detach(genre));
        var expectedGenres = IntStream.range(1, 4).boxed()
                .map(id -> new Genre(
                        id,
                        "Genre_" + id
                ))
                .toList();

        assertThat(actualGenres)
            .usingRecursiveFieldByFieldElementComparator()
            .containsExactlyElementsOf(expectedGenres);
    }
}
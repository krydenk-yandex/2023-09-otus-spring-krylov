package ru.otus.hw9.dto;

import java.util.List;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookSaveDto {

    @NotNull(message = "Название книги не должно быть пустым")
    @Size(min = 2, max = 50, message = "Название книги должно быть от 2 до 50 символов")
    private String title;

    @NotNull(message = "Жанры книги должны быть заполнены")
    @Size(min = 1, message = "У книги должен быть хотя бы 1 жанр")
    private List<Long> genresIds;

    @NotNull(message = "Автор книги должен быть заполнен")
    private Long authorId;
}

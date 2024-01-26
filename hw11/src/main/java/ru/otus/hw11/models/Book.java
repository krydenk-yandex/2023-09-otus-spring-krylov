package ru.otus.hw11.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.annotation.Id;

import java.util.List;

@Table("books")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    @Id
    private long id;

    private String title;

    private Author author;

    private List<Genre> genres;
}

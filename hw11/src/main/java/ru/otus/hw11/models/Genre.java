package ru.otus.hw11.models;


import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.annotation.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table("genres")
public class Genre {
    @Id
    private long id;

    private String name;
}
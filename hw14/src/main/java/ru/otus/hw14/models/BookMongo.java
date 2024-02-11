package ru.otus.hw14.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Document(collection = "books")
public class BookMongo {
    @Id
    private String id;

    private String title;

    @DBRef
    private AuthorMongo author;

    @DBRef
    private GenreMongo genre;
}

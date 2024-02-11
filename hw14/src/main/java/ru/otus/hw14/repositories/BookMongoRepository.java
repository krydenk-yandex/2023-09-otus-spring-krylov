package ru.otus.hw14.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw14.models.BookMongo;

public interface BookMongoRepository extends MongoRepository<BookMongo, String> {}

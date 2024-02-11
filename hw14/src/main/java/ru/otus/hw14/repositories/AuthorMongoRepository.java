package ru.otus.hw14.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw14.models.AuthorMongo;

public interface AuthorMongoRepository extends MongoRepository<AuthorMongo, String> {
    @Override
    boolean existsById(String id);
}

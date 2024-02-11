package ru.otus.hw14.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw14.models.CommentMongo;

public interface CommentMongoRepository extends MongoRepository<CommentMongo, String> {}

package ru.otus.hw14.service;

public interface MongoToRelationalServiceFactory {
    MongoToRelationalService getOrCreateService(String jobKey);
}

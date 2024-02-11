package ru.otus.hw14.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;

@Component
public class MongoToRelationalServiceFactoryImpl implements MongoToRelationalServiceFactory {

    private static Map<String, MongoToRelationalService> keysInstancesMap = new HashMap<>();

    @Override
    public MongoToRelationalService getOrCreateService(String key) {
        return Optional.ofNullable(keysInstancesMap.get(key))
                .orElseGet(() -> {
                    var newInstance = new MongoToRelationalService();
                    keysInstancesMap.put(key, newInstance);
                    return newInstance;
                });
    }
}

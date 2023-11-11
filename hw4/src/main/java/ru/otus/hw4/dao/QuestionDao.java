package ru.otus.hw4.dao;

import java.util.List;

import ru.otus.hw4.domain.Question;

public interface QuestionDao {
    List<Question> findAll();
}

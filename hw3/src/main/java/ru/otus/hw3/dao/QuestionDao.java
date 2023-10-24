package ru.otus.hw3.dao;

import java.util.List;

import ru.otus.hw3.domain.Question;

public interface QuestionDao {
    List<Question> findAll();
}

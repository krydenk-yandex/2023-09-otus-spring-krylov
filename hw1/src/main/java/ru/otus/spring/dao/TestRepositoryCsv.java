package ru.otus.spring.dao;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

import org.springframework.core.io.ClassPathResource;
import ru.otus.spring.domain.TestTask;
import ru.otus.spring.exception.TasksCsvMalformedException;
import ru.otus.spring.exception.TasksCsvReadException;

public final class TestRepositoryCsv implements TestTaskRepository {
    private String tasksResourceName;

    public String getTasksResourceName() {
        return tasksResourceName;
    }

    public void setTasksResourceName(String tasksResourceName) {
        this.tasksResourceName = tasksResourceName;
    }

    @Override
    public Collection<TestTask> getTasks() {
        var lines = readLines();

        return lines.stream().map(line -> {
            String[] fields = line.split("\s?,\s?");

            if (fields.length != 2) {
                throw new TasksCsvMalformedException();
            } else {
                return new TestTask(
                        fields[0] + "?",
                        fields[1]
                );
            }
        }).toList();
    }

    private Collection<String> readLines() {
        ClassPathResource resource = new ClassPathResource("/" + tasksResourceName);
        List<String> lines = new ArrayList<>(List.of());

        try (InputStream inputStream = resource.getInputStream()) {
            Scanner scanner = new Scanner(inputStream);

            while (scanner.hasNextLine()) {
                lines.add(scanner.nextLine());
            }
            scanner.close();
        } catch (IOException e) {
            throw new TasksCsvReadException(e);
        }

        return lines;
    }
}
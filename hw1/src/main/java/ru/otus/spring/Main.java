package ru.otus.spring;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.otus.spring.service.TestTaskService;

public class Main {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/spring-context.xml");
        TestTaskService service = context.getBean(TestTaskService.class);
        var tasks = service.getTasks();
        tasks.forEach(System.out::println);
    }
}
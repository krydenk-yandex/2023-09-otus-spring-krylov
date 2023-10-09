package ru.otus.spring;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.otus.spring.service.TestRunnerServiceImpl;

public class Main {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/spring-context.xml");
        TestRunnerServiceImpl service = context.getBean(TestRunnerServiceImpl.class);
        service.run();
    }
}
package ru.otus.hw4;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.otus.hw4.config.AppConfig;

@SpringBootApplication
@EnableConfigurationProperties(AppConfig.class)
public class ApplicationHW4 {
	public static void main(String[] args) {
		SpringApplication.run(ApplicationHW4.class, args);
	}
}

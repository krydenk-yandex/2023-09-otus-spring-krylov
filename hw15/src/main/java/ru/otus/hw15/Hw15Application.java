package ru.otus.hw15;

import java.util.List;
import java.util.stream.IntStream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.otus.hw15.integration.CiCdGateway;
import ru.otus.hw15.models.SourceCodeRepository;

@SpringBootApplication
public class Hw15Application {

	public static void main(String[] args) {
		var ctx = SpringApplication.run(Hw15Application.class, args);

		CiCdGateway gateway = ctx.getBean(CiCdGateway.class);

		var repositories = IntStream.range(1, 4).boxed()
				.map(i -> new SourceCodeRepository(
						String.format("https://some-domain.com/%d", i),
						List.of(
								String.format("username%d", (i - 1) * 2),
								String.format("username%d", (i - 1) * 2 + 1)
						)
				)).toList();

		gateway.process(repositories);
	}
}

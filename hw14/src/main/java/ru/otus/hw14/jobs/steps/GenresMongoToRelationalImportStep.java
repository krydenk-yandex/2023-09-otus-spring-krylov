package ru.otus.hw14.jobs.steps;

import java.util.HashMap;

import javax.sql.DataSource;

import lombok.AllArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.MongoPagingItemReader;
import org.springframework.batch.item.data.builder.MongoPagingItemReaderBuilder;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import ru.otus.hw14.models.GenreJdbc;
import ru.otus.hw14.models.GenreMongo;
import ru.otus.hw14.service.MongoToRelationalServiceFactory;

@SuppressWarnings("unused")
@Configuration
@AllArgsConstructor
public class GenresMongoToRelationalImportStep {
    private static final int CHUNK_SIZE = 10;

    private JobRepository jobRepository;

    private PlatformTransactionManager platformTransactionManager;
    
    @Bean
    public MongoPagingItemReader<GenreMongo> genreReader(
            MongoTemplate template
    ) {
        return new MongoPagingItemReaderBuilder<GenreMongo>()
                .name("genreItemReader")
                .template(template)
                .jsonQuery("{}")
                .targetType(GenreMongo.class)
                .sorts(new HashMap<>())
                .build();
    }

    
    @Bean
    @StepScope
    public ItemProcessor<GenreMongo, GenreJdbc> genreProcessor(
            MongoToRelationalServiceFactory mongoToRelationalServiceFactory,
            @Value("#{jobParameters['jobKey']}") String jobKey
    ) {
        return mongoToRelationalServiceFactory.getOrCreateService(jobKey)::convertGenre;
    }

    @Bean
    public JdbcBatchItemWriter<GenreJdbc> genreWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<GenreJdbc>()
                .dataSource(dataSource)
                .sql("""
                    insert into genres(id, name)
                    values (:id, :name)
                """)
                .beanMapped()
                .build();
    }
    
    @Bean
    public Step importGenresFromMongoToRelationalStep(
            ItemReader<GenreMongo> reader,
            ItemWriter<GenreJdbc> writer,
            ItemProcessor<GenreMongo, GenreJdbc> itemProcessor
    ) {
        return new StepBuilder("importGenresFromMongoToRelationalStep", jobRepository)
                .<GenreMongo, GenreJdbc>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(reader)
                .processor(itemProcessor)
                .writer(writer)
                .taskExecutor(new SimpleAsyncTaskExecutor())
                .build();
    }
}

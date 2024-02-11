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
import ru.otus.hw14.models.AuthorJdbc;
import ru.otus.hw14.models.AuthorMongo;
import ru.otus.hw14.service.MongoToRelationalServiceFactory;

@SuppressWarnings("unused")
@Configuration
@AllArgsConstructor
public class AuthorsMongoToRelationalImportStep {
    private static final int CHUNK_SIZE = 10;

    private JobRepository jobRepository;

    private PlatformTransactionManager platformTransactionManager;

    
    @Bean
    public MongoPagingItemReader<AuthorMongo> authorReader(
            MongoTemplate template
    ) {
        return new MongoPagingItemReaderBuilder<AuthorMongo>()
                .name("authorItemReader")
                .template(template)
                .jsonQuery("{}")
                .targetType(AuthorMongo.class)
                .sorts(new HashMap<>())
                .build();
    }

    
    @Bean
    @StepScope
    public ItemProcessor<AuthorMongo, AuthorJdbc> authorProcessor(
            MongoToRelationalServiceFactory mongoToRelationalServiceFactory,
            @Value("#{jobParameters['jobKey']}") String jobKey
    ) {
        return mongoToRelationalServiceFactory.getOrCreateService(jobKey)::convertAuthor;
    }

    
    @Bean
    public JdbcBatchItemWriter<AuthorJdbc> authorWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<AuthorJdbc>()
                .dataSource(dataSource)
                .sql("""
                    insert into authors(id, full_name)
                    values (:id, :fullName)
                """)
                .beanMapped()
                .build();
    }

    @Bean
    public Step importAuthorsFromMongoToRelationalStep(
            ItemReader<AuthorMongo> reader,
            ItemWriter<AuthorJdbc> writer,
            ItemProcessor<AuthorMongo, AuthorJdbc> itemProcessor
    ) {
        return new StepBuilder("importAuthorsFromMongoToRelationalStep", jobRepository)
                .<AuthorMongo, AuthorJdbc>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(reader)
                .processor(itemProcessor)
                .writer(writer)
                .taskExecutor(new SimpleAsyncTaskExecutor())
                .build();
    }
}

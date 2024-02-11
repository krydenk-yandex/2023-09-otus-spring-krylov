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
import ru.otus.hw14.models.BookJdbc;
import ru.otus.hw14.models.BookMongo;
import ru.otus.hw14.service.MongoToRelationalServiceFactory;


@SuppressWarnings("unused")
@Configuration
@AllArgsConstructor
public class BooksMongoToRelationalImportStep {
    private static final int CHUNK_SIZE = 10;

    private JobRepository jobRepository;

    private PlatformTransactionManager platformTransactionManager;

    
    @Bean
    public MongoPagingItemReader<BookMongo> bookReader(
            MongoTemplate template
    ) {
        return new MongoPagingItemReaderBuilder<BookMongo>()
                .name("bookItemReader")
                .template(template)
                .jsonQuery("{}")
                .targetType(BookMongo.class)
                .sorts(new HashMap<>())
                .build();
    }


    @Bean
    @StepScope
    public ItemProcessor<BookMongo, BookJdbc> bookProcessor(
            MongoToRelationalServiceFactory mongoToRelationalServiceFactory,
            @Value("#{jobParameters['jobKey']}") String jobKey
    ) {
        return mongoToRelationalServiceFactory.getOrCreateService(jobKey)::convertBook;
    }
    
    @Bean
    public JdbcBatchItemWriter<BookJdbc> bookWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<BookJdbc>()
                .dataSource(dataSource)
                .sql("""
                    insert into books(id, title, author_id, genre_id)
                    values (:id, :title, :authorId, :genreId)
                """)
                .beanMapped()
                .build();
    }
    
    @Bean
    public Step importBooksFromMongoToRelationalStep(
            ItemReader<BookMongo> reader,
            ItemWriter<BookJdbc> writer,
            ItemProcessor<BookMongo, BookJdbc> itemProcessor
    ) {
        return new StepBuilder("importBooksFromMongoToRelationalStep", jobRepository)
                .<BookMongo, BookJdbc>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(reader)
                .processor(itemProcessor)
                .writer(writer)
                .taskExecutor(new SimpleAsyncTaskExecutor())
                .build();
    }
}

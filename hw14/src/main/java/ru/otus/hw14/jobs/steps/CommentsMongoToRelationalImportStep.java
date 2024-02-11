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
import ru.otus.hw14.models.CommentJdbc;
import ru.otus.hw14.models.CommentMongo;
import ru.otus.hw14.service.MongoToRelationalServiceFactory;


@SuppressWarnings("unused")
@Configuration
@AllArgsConstructor
public class CommentsMongoToRelationalImportStep {
    private static final int CHUNK_SIZE = 10;

    private JobRepository jobRepository;

    private PlatformTransactionManager platformTransactionManager;
    
    @Bean
    public MongoPagingItemReader<CommentMongo> commentReader(
            MongoTemplate template
    ) {
        return new MongoPagingItemReaderBuilder<CommentMongo>()
                .name("commentItemReader")
                .template(template)
                .jsonQuery("{}")
                .targetType(CommentMongo.class)
                .sorts(new HashMap<>())
                .build();
    }
    
    @Bean
    @StepScope
    public ItemProcessor<CommentMongo, CommentJdbc> commentProcessor(
            MongoToRelationalServiceFactory mongoToRelationalServiceFactory,
            @Value("#{jobParameters['jobKey']}") String jobKey
    ) {
        return mongoToRelationalServiceFactory.getOrCreateService(jobKey)::convertComment;
    }
    
    @Bean
    public JdbcBatchItemWriter<CommentJdbc> commentWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<CommentJdbc>()
                .dataSource(dataSource)
                .sql("""
                    insert into comments(id, text, author_name, book_id)
                    values (:id, :text, :authorName, :bookId)
                """)
                .beanMapped()
                .build();
    }
    
    @Bean
    public Step importCommentsFromMongoToRelationalStep(
            ItemReader<CommentMongo> reader,
            ItemWriter<CommentJdbc> writer,
            ItemProcessor<CommentMongo, CommentJdbc> itemProcessor
    ) {
        return new StepBuilder("importCommentsFromMongoToRelationalStep", jobRepository)
                .<CommentMongo, CommentJdbc>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(reader)
                .processor(itemProcessor)
                .writer(writer)
                .taskExecutor(new SimpleAsyncTaskExecutor())
                .build();
    }
}

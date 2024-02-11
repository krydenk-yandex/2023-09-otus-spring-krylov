package ru.otus.hw14.jobs;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SuppressWarnings("unused")
@Configuration
public class MongoToRelationalLibraryImportJobConfig {

    public static final String IMPORT_LIBRARY_JOB_NAME = "importLibraryJob";

    private static final int CHUNK_SIZE = 5;

    @Autowired
    private JobRepository jobRepository;

    @Bean
    public Job mongoToRelationalLibraryImportJob(
            Step importAuthorsFromMongoToRelationalStep,
            Step importGenresFromMongoToRelationalStep,
            Step importBooksFromMongoToRelationalStep,
            Step importCommentsFromMongoToRelationalStep
    ) {
        return new JobBuilder(IMPORT_LIBRARY_JOB_NAME, jobRepository)
                .incrementer(new RunIdIncrementer())
                .flow(importAuthorsFromMongoToRelationalStep)
                .next(importGenresFromMongoToRelationalStep)
                .next(importBooksFromMongoToRelationalStep)
                .next(importCommentsFromMongoToRelationalStep)
                .end()
                .build();
    }
}

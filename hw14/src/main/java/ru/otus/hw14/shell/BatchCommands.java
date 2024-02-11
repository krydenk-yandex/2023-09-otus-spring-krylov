package ru.otus.hw14.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw14.jobs.MongoToRelationalLibraryImportJobConfig;
import org.h2.tools.Console;

import java.sql.SQLException;
import java.util.Date;
import java.util.Properties;

@RequiredArgsConstructor
@ShellComponent
public class BatchCommands {

    private final JobOperator jobOperator;

    private final JobExplorer jobExplorer;


    @SuppressWarnings("unused")
    @ShellMethod(value = "startMigrationJobWithJobOperator", key = "migrate")
    public void startMigrationJobWithJobOperator() throws Exception {
        Properties properties = new Properties();
        properties.setProperty(
                "jobKey",
                MongoToRelationalLibraryImportJobConfig.IMPORT_LIBRARY_JOB_NAME + (new Date()).getTime()
        );

        Long executionId = jobOperator.start(
                MongoToRelationalLibraryImportJobConfig.IMPORT_LIBRARY_JOB_NAME,
                properties
        );
        System.out.println(jobOperator.getSummary(executionId));
    }

    @SuppressWarnings("unused")
    @ShellMethod(value = "showJobsInfo", key = "jobs-info")
    public void showJobsInfo() {
        System.out.println(jobExplorer.getJobNames());
        System.out.println(jobExplorer.getLastJobInstance(
                MongoToRelationalLibraryImportJobConfig.IMPORT_LIBRARY_JOB_NAME));
    }

    @ShellMethod(value = "Show h2 Web console", key = "h2")
    public String showH2Console() {
        try {
            Console.main();
        } catch (SQLException e) {
            throw new RuntimeException("An error occurred on H2 Console", e);
        }
        return "H2 Web server is running, go to browser to open in (default is http://localhost:8082)";
    }
}

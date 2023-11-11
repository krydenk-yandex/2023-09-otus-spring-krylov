package ru.otus.hw4.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import ru.otus.hw4.service.LocalizedMessagesService;
import ru.otus.hw4.service.TestRunnerService;

@ShellComponent
@RequiredArgsConstructor
public class ApplicationCommands {
    private final TestRunnerService testRunnerService;

    private final LocalizedMessagesService localizedMessagesService;

    @ShellMethod(value = "Student authentication", key = {"a", "auth"})
    public String putStudent() {
        testRunnerService.askStudentForNameAndSurname();
        return "\n" + localizedMessagesService.getMessage("Shell.auth.finish");
    }

    @ShellMethod(value = "Start test command", key = {"t", "start-test"})
    @ShellMethodAvailability(value = "isTestRunAvailable")
    public String startTest() {
        testRunnerService.run();
        return "\n" + localizedMessagesService.getMessage("Shell.test.finish");
    }

    @ShellMethod(value = "Quit test session", key = {"q", "quit"})
    public String forgetStudent() {
        testRunnerService.cleanStudent();
        return "\n" + localizedMessagesService.getMessage("Shell.quit.finish");
    }

    private Availability isTestRunAvailable() {
        return testRunnerService.getStudent() == null
            ? Availability.unavailable(localizedMessagesService.getMessage("Shell.not.authenticated"))
            : Availability.available();
    }
}

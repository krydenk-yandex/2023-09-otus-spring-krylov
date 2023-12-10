package ru.otus.hw7.commands;

import java.sql.SQLException;

import lombok.RequiredArgsConstructor;
import org.h2.tools.Console;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@RequiredArgsConstructor
@ShellComponent
public class H2Commands {

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

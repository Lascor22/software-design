package ru.itmo.sd.tasks;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import ru.itmo.sd.tasks.configuration.WebAppConfig;

@Import(WebAppConfig.class)
@SpringBootApplication
public class Run {
    static {
        try {
            PropertiesLoader.load(Run.class.getResourceAsStream("/application.properties"));
        } catch (IOException ioe) {
            System.err.println("Failed to load properties file");
            System.exit(1);
        }
    }

    public static void main(String... args) {
        SpringApplication.run(Run.class, args);
    }
}

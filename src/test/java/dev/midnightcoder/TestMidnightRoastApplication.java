package dev.midnightcoder;

import dev.midnightcoder.app.Application;
import org.springframework.boot.SpringApplication;

public class TestMidnightRoastApplication {

    public static void main(String[] args) {
        SpringApplication.from(Application::main)
            .with(TestcontainersConfiguration.class)
            .run(args);
    }

}

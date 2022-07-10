package test.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication @ComponentScan(basePackageClasses = Application.class)
public class Application {
    public static void main(String[] args) {
        new SpringApplication(Application.class).run(args);
    }
}

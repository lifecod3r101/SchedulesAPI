package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {"org.example.Repositories"})
@EntityScan(basePackages = {"org.example.Models"})
public class SchedulesAPIApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(SchedulesAPIApplication.class, args);
    }
}

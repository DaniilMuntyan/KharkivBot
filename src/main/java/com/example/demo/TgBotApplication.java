package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@EnableAsync
public class TgBotApplication {
    public static void main(String[] args) {
        SpringApplication.run(TgBotApplication.class, args);
    }
}

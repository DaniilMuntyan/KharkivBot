package com.example.demo;

import com.example.demo.user_bot.cache.UserCache;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PreDestroy;

@SpringBootApplication
@EnableAsync
@EnableScheduling
public class TgBotApplication {
    public static void main(String[] args) {
        SpringApplication.run(TgBotApplication.class, args);
    }
}

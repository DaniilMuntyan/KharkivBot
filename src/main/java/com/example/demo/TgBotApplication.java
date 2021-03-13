package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableScheduling
public class TgBotApplication {
    public static void main(String[] args) {
        SpringApplication.run(TgBotApplication.class, args);
        /*UserBotSendingQueue userBotSendingQueue = BeanUtil.getBean(UserBotSendingQueue.class);
        userBotSendingQueue.startQueue();
        AdminBotChannelQueue adminBotChannelQueue = BeanUtil.getBean(AdminBotChannelQueue.class);
        adminBotChannelQueue.startQueue();*/
    }
}

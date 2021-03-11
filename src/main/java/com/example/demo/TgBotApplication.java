package com.example.demo;

import com.example.demo.admin_bot.queue.AdminBotChannelQueue;
import com.example.demo.user_bot.queue.UserBotSendingQueue;
import com.example.demo.common_part.utils.BeanUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

import java.util.LinkedList;

@SpringBootApplication
@EnableAsync
public class TgBotApplication {
    public static void main(String[] args) {
        SpringApplication.run(TgBotApplication.class, args);
        UserBotSendingQueue userBotSendingQueue = BeanUtil.getBean(UserBotSendingQueue.class);
        userBotSendingQueue.startQueue();
        AdminBotChannelQueue adminBotChannelQueue = BeanUtil.getBean(AdminBotChannelQueue.class);
        adminBotChannelQueue.startQueue();
    }

    @Bean
    public LinkedList<BotApiMethod<?>> createQueue() {
        return new LinkedList<>();
    }

}

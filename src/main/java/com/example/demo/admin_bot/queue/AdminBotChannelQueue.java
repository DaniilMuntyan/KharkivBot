package com.example.demo.admin_bot.queue;

import com.example.demo.admin_bot.botapi.AdminTelegramBot;
import com.example.demo.common_part.utils.BeanUtil;
import com.example.demo.common_part.constants.ProgramVariables;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.util.LinkedList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class AdminBotChannelQueue {
    private static final Logger LOGGER = Logger.getLogger(AdminBotChannelQueue.class);
    private final ScheduledExecutorService scheduleMessages = Executors.newSingleThreadScheduledExecutor();
    private final LinkedList<SendMessage> messagesQueue = new LinkedList<>();
    private final ProgramVariables programVariables;

    @Autowired
    public AdminBotChannelQueue(ProgramVariables programVariables) {
        this.programVariables = programVariables;
    }

    @Async
    public void startQueue() {
        final AdminTelegramBot bot = BeanUtil.getBean(AdminTelegramBot.class);

        // Обрабатываю постинг в канал раз в определенное время
        Runnable sendMessageQueueLooper = () -> {
            if (this.messagesQueue.size() != 0) {
                SendMessage partialBotApiMethod = this.messagesQueue.pollFirst();
                this.sendMessage(partialBotApiMethod, bot);
            }
        };

        this.scheduleMessages.scheduleWithFixedDelay(sendMessageQueueLooper, 0, programVariables.getDelayChannelMessage(), TimeUnit.MILLISECONDS);
    }

    public void addMessageToQueue(SendMessage partialBotApiMethod) {
        this.messagesQueue.add(partialBotApiMethod);
    }

    private void sendMessage(SendMessage message, TelegramLongPollingBot bot) {
        try {
            bot.execute(message);
            LOGGER.info("SENT MESSAGE");
        } catch (TelegramApiRequestException e) {
            LOGGER.error(e);
            if (e.getErrorCode().equals(429)) {
                this.messagesQueue.addFirst(message);
                LOGGER.info("ADDED TO QUEUE!");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException interruptedException) {
                    LOGGER.error(interruptedException);
                    interruptedException.printStackTrace();
                }
            }
            e.printStackTrace();
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
package com.example.demo.user_bot.schedule;

import com.example.demo.common_part.constants.ProgramVariables;
import com.example.demo.user_bot.botapi.RentalTelegramBot;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Service
@PropertySource("classpath:program.properties")
public class UserBotSendingQueue {
    private static final Logger LOGGER = Logger.getLogger(UserBotSendingQueue.class);

    //private final ScheduledExecutorService scheduleMessages = Executors.newSingleThreadScheduledExecutor();
    //private final ScheduledExecutorService scheduleMethods = Executors.newSingleThreadScheduledExecutor();
    private final LinkedList<SendMessage> messagesQueue = new LinkedList<>();
    private final LinkedList<SendMessage> bulkMessagesQueue = new LinkedList<>();
    private final LinkedList<BotApiMethod<?>> apiQueue = new LinkedList<>();
    private final ProgramVariables programVariables;
    private final RentalTelegramBot bot;

    @Autowired
    public UserBotSendingQueue(ProgramVariables programVariables, @Lazy RentalTelegramBot bot) {
        this.programVariables = programVariables;
        this.bot = bot;
    }

    @Scheduled(fixedDelayString = "${delay.user.message}")
    public void sendMessageLooper() {
        // Обрабатываю отправку сообщений каждые X секунд
        // Если отвечаем юзеру сообщением (первый приоритет)
        if (this.messagesQueue.size() != 0) {
            SendMessage newMessage = this.messagesQueue.pollFirst();
            this.sendMessage(newMessage, bot);
        } else { // Если происходит рассылка (второй приоритет)
            if (this.bulkMessagesQueue.size() != 0) {
                SendMessage newMessage = this.bulkMessagesQueue.pollFirst();
                this.sendMessage(newMessage, bot);
            }
        }
    }

    @Scheduled(fixedDelayString = "${delay.user.api}")
    public void sendApiMethodLooper() {
        // Обрабатываю АПИ запросы каждые X секунд
        if (this.apiQueue.size() != 0) {
            BotApiMethod<?> botApiMethod = this.apiQueue.pollFirst();
            this.executeMethod(botApiMethod, bot);
        }
    }

    /*long time1 = System.currentTimeMillis(), time2 = System.currentTimeMillis();
        int c = 0;
        while(true) {
            if (!this.messagesQueue.isEmpty()) {
                if (c == 0) {
                    time1 = System.currentTimeMillis();
                }
                time2 = System.currentTimeMillis();

                // Если прошло больше одной секунды - обнуляем счетчик
                if (time1 - time2 > 1000) {
                    c = 0;
                }

                // Если за меньшее время, чем 1 секунда,
                // набралось 30 сообщений - засыпаем, пока не начнется следующая секунда
                if (c > 30 && (time1 - time2 < 1000)) {
                    try {
                        Thread.sleep(1000 - (time1 - time2));
                        c = 0;
                    } catch (InterruptedException e) {
                        LOGGER.error(e);
                        e.printStackTrace();
                    }
                } else {
                    SendMessage newMessage = this.messagesQueue.pollFirst();
                    this.sendMessage(newMessage, bot);
                    c++;
                }
            }
        }*/


    /*@Async
    public void startQueue() {
        final RentalTelegramBot bot = BeanUtil.getBean(RentalTelegramBot.class);

        // Обрабатываю отправку сообщений каждые X секунд
        Runnable sendMessageQueueLooper = () -> {
            // Если отвечаем юзеру сообщением (первый приоритет)
            if (this.messagesQueue.size() != 0) {
                SendMessage newMessage = this.messagesQueue.pollFirst();
                this.sendMessage(newMessage, bot);
            } else { // Если происходит рассылка (второй приоритет)
                if (this.bulkMessagesQueue.size() != 0) {
                    SendMessage newMessage = this.bulkMessagesQueue.pollFirst();
                    this.sendMessage(newMessage, bot);
                }
            }
        };

        // Обрабатываю АПИ запросы каждые X секунд
        Runnable sendMethodQueueLooper = () -> {
            if (this.apiQueue.size() != 0) {
                BotApiMethod<?> botApiMethod = this.apiQueue.pollFirst();
                this.executeMethod(botApiMethod, bot);
            }
        };

        // Выполнение по расписанию
        this.scheduleMessages.scheduleWithFixedDelay(sendMessageQueueLooper, 0, programVariables.getDelayUserMessage(), TimeUnit.MILLISECONDS);
        this.scheduleMethods.scheduleWithFixedDelay(sendMethodQueueLooper, 0, programVariables.getDelayUserApi(), TimeUnit.MILLISECONDS);

    }*/

    public void addBulkMessageToQueue(SendMessage sendMessage) {
        this.bulkMessagesQueue.add(sendMessage);
    }

    public void addMessageToQueue(SendMessage partialBotApiMethod) {
        this.messagesQueue.add(partialBotApiMethod);
    }

    public void addAllMessages(List<SendMessage> partialBotApiMethodList) {
        this.messagesQueue.addAll(partialBotApiMethodList);
    }

    public void addMethodToQueue(BotApiMethod<?> partialBotApiMethod) {
        this.apiQueue.add(partialBotApiMethod);
    }

    public void addAllMethods(List<BotApiMethod<?>> partialBotApiMethodList) {
        this.apiQueue.addAll(partialBotApiMethodList);
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

    private void executeMethod(BotApiMethod<?> method, TelegramLongPollingBot bot) {
        try {
            bot.execute(method);
            LOGGER.info("SENT METHOD: " + method.getMethod());
        } catch (TelegramApiRequestException e) {
            LOGGER.error(e);
            if (e.getErrorCode().equals(429)) {
                this.apiQueue.addFirst(method);
                LOGGER.info("METHOD ADDED TO QUEUE!");
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
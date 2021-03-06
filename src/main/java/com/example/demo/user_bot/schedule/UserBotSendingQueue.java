package com.example.demo.user_bot.schedule;

import com.example.demo.common_part.constants.ProgramVariables;
import com.example.demo.user_bot.botapi.RentalTelegramBot;
import com.example.demo.user_bot.cache.DataCache;
import com.example.demo.user_bot.service.entities.UserService;
import com.example.demo.user_bot.utils.MenuSendMessage;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.meta.updateshandlers.SentCallback;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
@PropertySource("classpath:program.properties")
public class UserBotSendingQueue {
    private static final Logger LOGGER = Logger.getLogger(UserBotSendingQueue.class);

    private final LinkedList<SendMessage> messagesQueue = new LinkedList<>();
    private final LinkedList<SendMessage> bulkMessagesQueue = new LinkedList<>();
    private final LinkedList<BotApiMethod<?>> apiQueue = new LinkedList<>();
    private final ProgramVariables programVariables;
    private final DataCache dataCache;
    private final RentalTelegramBot bot;
    private final UserService userService;

    private final TaskExecutor taskExecutor;

    @Autowired
    public UserBotSendingQueue(ProgramVariables programVariables, DataCache dataCache, @Lazy RentalTelegramBot bot, UserService userService, @Qualifier("taskExecutor") TaskExecutor taskExecutor) {
        this.programVariables = programVariables;
        this.dataCache = dataCache;
        this.bot = bot;
        this.userService = userService;
        this.taskExecutor = taskExecutor;
        this.loop(); // Запускаю в другом потоке - для очередей
    }

    public void loop() {
        Runnable sendMessageQueueLooper = () -> {
            try {
                LOGGER.info("INFINITE");
                long timeMsg1 = System.currentTimeMillis(), timeMsg2 = System.currentTimeMillis(); // Для замера отправок сообщений
                long timeApi1 = timeMsg1, timeApi2 = timeMsg2; // Для замера отправок АПИ методов
                int c = 0; // Для сообщений
                int k = 0; // Для АПИ методов
                boolean hasApiMethod;
                LOGGER.info("STARTING LOOP...");
                while (Thread.currentThread().isAlive()) { // Пока поток жив - выполняю
                    while(!this.apiQueue.isEmpty()) { // Пока есть неотправленные АПИ методы
                        if (k == 0) {
                            timeApi1 = System.currentTimeMillis();
                        }
                        timeApi2 = System.currentTimeMillis();

                        if (timeApi2 - timeApi1 > 1000) { // Прошло больше секунды => обнуляем счетчик
                            k = 0;
                        }

                        // Если за меньшее время, чем 1 секунда, набралось N api запросов - ничего не делаю
                        if (k > programVariables.getMaxApiPerSecond() && (timeApi2 - timeApi1 < 1000)) {
                            LOGGER.info("!!! НАБРАЛИ МАКСИМУМ API ЗАПРОСОВ В СЕКУНДУ! Прошло: " +
                                    (timeApi2 - timeApi1) + "мс");
                            break; // Выхожу из цикла, так как отправлять больше нельзя
                        } else { // Если по ограничениям все нормально - отправляю апи запрос
                            BotApiMethod<?> botApiMethod = this.apiQueue.pollFirst();
                            this.executeMethod(botApiMethod, bot);
                            k++;
                        }
                    }

                    while(!this.messagesQueue.isEmpty() || !this.bulkMessagesQueue.isEmpty()) {
                        if (c == 0) {
                            timeMsg1 = System.currentTimeMillis();
                        }
                        timeMsg2 = System.currentTimeMillis();

                        // Если прошло больше одной секунды - обнуляем счетчик
                        if (timeMsg2 - timeMsg1 > 1000) {
                            c = 0;
                        }

                        // Если за меньшее время, чем 1 секунда, набралось N сообщений - ничего не делаем
                        if (c > programVariables.getMaxMsgPerSecond() && (timeMsg2 - timeMsg1 < 1000)) {
                            LOGGER.info("!!! НАБРАЛИ МАКСИМУМ СООБЩЕНИЙ В СЕКУНДУ! Прошло: " +
                                    (timeMsg2 - timeMsg1) + "мс");
                            break; // Больше отправлять нельзя
                        } else { // Если по ограничениям все нормально
                            if (!this.messagesQueue.isEmpty()) { // Если есть личные сообщения - отправляю их (первый приоритет)
                                SendMessage newMessage = this.messagesQueue.pollFirst();
                                this.sendMessage(newMessage, bot);
                                c++;
                            } else {
                                if (this.bulkMessagesQueue.size() != 0) { // Если есть что рассылать
                                    SendMessage newMessage = this.bulkMessagesQueue.pollFirst();
                                    this.sendMessage(newMessage, bot);
                                    c++;
                                }
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                LOGGER.error(ex);
                ex.printStackTrace();
                this.loop(); // Опять пытаюсь запустить цикл
            }
            LOGGER.info("END LOOP!!!");
        };
        this.taskExecutor.execute(sendMessageQueueLooper); // Запускаю цикл в потоке для расписаний
    }

    public void addBulkMessageToQueue(SendMessage sendMessage) {
        this.bulkMessagesQueue.add(sendMessage);
    }

    public void addMessageToQueue(SendMessage partialBotApiMethod) {
        this.messagesQueue.add(partialBotApiMethod);
    }

    public void addMethodToQueue(BotApiMethod<?> partialBotApiMethod) {
        this.apiQueue.add(partialBotApiMethod);
    }

    public void sendMessage(SendMessage message, TelegramLongPollingBot bot) {
        try {
            long time1 = System.currentTimeMillis();
            if (message instanceof MenuSendMessage) { // Если отправляем новое меню
                bot.executeAsync(message, new SaveMenuIdCallback(this.dataCache));
            } else { // Если отправляю обычное сообщение
                try {
                    //LOGGER.info("TO " + message.getChatId());
                    bot.executeAsync(message);
                } catch (Exception e) {
                    LOGGER.info(e.toString());
                }
            }
            /*Message newMenuMessage = bot.execute(message);
            if (message instanceof MenuSendMessage) { // Если отправляем новое меню
                LOGGER.info("NEW MENU");
                Message newMenuMessage = completableFuture.get();
                // Устанавливаю новое значение menuMessageId для пользователя, если нужно
                if (((MenuSendMessage) message).isChangeMenuMessageId()) {
                    this.dataCache.setMenuMsgId(message.getChatId(), newMenuMessage.getMessageId());
                }
            }*/
            LOGGER.info("TIME execute user sendMessage: " + (System.currentTimeMillis() - time1));
        } catch (TelegramApiRequestException e) {
            LOGGER.error(e);
            if (this.blocked(e.toString())) { // Если юзер заблокировал бота
                this.userService.deleteUser(Long.valueOf(message.getChatId()));
            }
            if (e.getErrorCode().equals(429)) {
                this.messagesQueue.addFirst(message);
                LOGGER.info("ADDED TO QUEUE: " + message.getChatId());
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException interruptedException) {
                    LOGGER.error(interruptedException);
                    interruptedException.printStackTrace();
                }
                e.printStackTrace();
            }
        } catch (TelegramApiException e/*| InterruptedException | ExecutionException e*/) {
            LOGGER.info("TelegramApiException: " + message.getChatId());
            LOGGER.error(e);
            //e.printStackTrace();
        }
    }

    private void executeMethod(BotApiMethod<?> method, TelegramLongPollingBot bot) {
        try {
            long time1 = System.currentTimeMillis();
            //LOGGER.info(method.getMethod());
            bot.executeAsync(method); // TODO: изменил на async
            LOGGER.info("TIME execute user method " + method.getMethod() + ": " +
                    (System.currentTimeMillis() - time1));
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
            LOGGER.info("method: " + method.getMethod());
            LOGGER.error(e);
            //e.printStackTrace();
        }
    }

    private boolean blocked(String exception) {
        return exception.contains("Forbidden") && exception.contains("bot") && exception.contains("blocked") &&
                exception.contains("by the user");
    }
}
package com.example.demo.user_bot.schedule;

import com.example.demo.common_part.constants.ProgramVariables;
import com.example.demo.common_part.utils.BeanUtil;
import com.example.demo.user_bot.botapi.RentalTelegramBot;
import com.example.demo.user_bot.cache.DataCache;
import com.example.demo.user_bot.utils.MenuSendMessage;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

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

    private final TaskExecutor taskExecutor;

    @Autowired
    public UserBotSendingQueue(ProgramVariables programVariables, DataCache dataCache, @Lazy RentalTelegramBot bot, @Qualifier("taskExecutor") TaskExecutor taskExecutor) {
        this.programVariables = programVariables;
        this.dataCache = dataCache;
        this.bot = bot;
        this.taskExecutor = taskExecutor;
        this.loop(); // Запускаю в другом потоке - для очередей
    }

    private void printDate(String text, long milliseconds) {
        Date date = new Date(milliseconds);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        LOGGER.info(text + calendar.get(Calendar.HOUR_OF_DAY) + ":" +
                calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND) + ":" +
                calendar.get(Calendar.MILLISECOND));
    }

    //@SuppressWarnings("InfiniteLoopStatement")
    public void loop() {
        Runnable sendMessageQueueLooper = () -> {
            LOGGER.info("INFINITE");
            long time1 = System.currentTimeMillis(), time2 = System.currentTimeMillis(); // Для замера отправок сообщений
            long timeApi1 = time1, timeApi2 = time2; // Для замера отправок АПИ методов
            int c = 0; // Для сообщений
            int k = 0; // Для АПИ методов
            boolean hasApiMethod;
            try {
                LOGGER.info("STARTING LOOP...");
                while (Thread.currentThread().isAlive()) {
                    hasApiMethod = !this.apiQueue.isEmpty();
                    if (hasApiMethod) { // Если есть апи запросы, которые надо отправить
                        LOGGER.info("API QUEUE IS NOT EMPTY");
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
                        } else { // Если по ограничениям все нормально - отправляю апи запрос
                            BotApiMethod<?> botApiMethod = this.apiQueue.pollFirst();
                            this.executeMethod(botApiMethod, bot);
                            k++;
                        }
                    }

                    // Если есть какие-либо сообщения юзерам
                    if (!this.messagesQueue.isEmpty() || !this.bulkMessagesQueue.isEmpty()) {
                        LOGGER.info("MESSAGES QUEUE IS NOT EMPTY");
                        if (c == 0) {
                            time1 = System.currentTimeMillis();
                        }
                        time2 = System.currentTimeMillis();

                        // Если прошло больше одной секунды - обнуляем счетчик
                        if (time2 - time1 > 1000) {
                            c = 0;
                        }

                        // Если за меньшее время, чем 1 секунда, набралось N сообщений - ничего не делаем
                        if (c > programVariables.getMaxMsgPerSecond() && (time2 - time1 < 1000)) {
                            LOGGER.info("!!! НАБРАЛИ МАКСИМУМ СООБЩЕНИЙ В СЕКУНДУ! Прошло: " +
                                    (time2 - time1) + "мс");
                        } else { // Если по ограничениям все нормально
                            if (!this.messagesQueue.isEmpty()) { // Если есть личные сообщения - отправляю их (первый приоритет)
                                LOGGER.info("Есть личное сообщения - отправляю");
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
        this.taskExecutor.execute(sendMessageQueueLooper); // Запускаю цикл в другом потоке
    }

    /*@Scheduled(fixedDelayString = "${delay.user.message}")
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
    }*/

    /*@Scheduled(fixedDelayString = "${delay.user.api}")
    public void sendApiMethodLooper() {
        // Обрабатываю АПИ запросы каждые X секунд
        if (this.apiQueue.size() != 0) {
            BotApiMethod<?> botApiMethod = this.apiQueue.pollFirst();
            this.executeMethod(botApiMethod, bot);
        }
    }*/

    public void addBulkMessageToQueue(SendMessage sendMessage) {
        this.bulkMessagesQueue.add(sendMessage);
    }

    public void addMessageToQueue(SendMessage partialBotApiMethod) {
        //LOGGER.info("ADD TO MESSAGE QUEUE: " + partialBotApiMethod);
        this.messagesQueue.add(partialBotApiMethod);
    }

    public void addMethodToQueue(BotApiMethod<?> partialBotApiMethod) {
        this.apiQueue.add(partialBotApiMethod);
    }

    private void sendMessage(SendMessage message, TelegramLongPollingBot bot) {
        try {
            LOGGER.info("SENDING MESSAGE...");
            long time1 = System.currentTimeMillis();
            Message newMenuMessage = bot.execute(message);
            LOGGER.info("TIME execute user SendMessage: " + (System.currentTimeMillis() - time1));
            LOGGER.info("New sent message: " + newMenuMessage);
            if (message instanceof MenuSendMessage) { // Если отправляем новое меню
                // Устанавливаю новое значение menuMessageId для пользователя, если нужно
                if (((MenuSendMessage) message).isChangeMenuMessageId()) {
                    this.dataCache.setMenuMsgId(message.getChatId(), newMenuMessage.getMessageId());
                    //LOGGER.info("УСТАНАВЛИВАЮ МЕНЮ: " + newMenuMessage.getMessageId());
                }
            }
        } catch (TelegramApiRequestException e) {
            LOGGER.error(e);
            if (e.getErrorCode().equals(429)) {
                this.messagesQueue.addFirst(message);
                LOGGER.info("ADDED TO QUEUE: " + message.getChatId());
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
            long time1 = System.currentTimeMillis();
            bot.execute(method);
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
            e.printStackTrace();
        }
    }
}
package com.example.demo.user_bot.schedule;

import com.example.demo.user_bot.cache.DataCache;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.meta.updateshandlers.SentCallback;

// Callback для
public final class SaveMenuIdCallback implements SentCallback<Message> {
    private static final Logger LOGGER = Logger.getLogger(SaveMenuIdCallback.class);

    private final DataCache dataCache;

    public SaveMenuIdCallback(DataCache dataCache) {
        this.dataCache = dataCache;
    }

    @Override
    public void onResult(BotApiMethod<Message> botApiMethod, Message message) {
        // Устанавливаю айди меню для юзера
        this.dataCache.setMenuMsgId(message.getChatId().toString(), message.getMessageId());
    }

    @Override
    public void onError(BotApiMethod<Message> botApiMethod, TelegramApiRequestException e) {
        LOGGER.error(e);
        e.printStackTrace();
    }

    @Override
    public void onException(BotApiMethod<Message> botApiMethod, Exception e) {
        LOGGER.error(e);
        e.printStackTrace();
    }
}

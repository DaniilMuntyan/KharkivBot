package com.example.demo.user_bot.service.handler.callback;

import com.example.demo.common_part.constants.UserMenuVariables;
import com.example.demo.user_bot.cache.UserCache;
import com.example.demo.user_bot.service.handler.callback.init.*;
import com.example.demo.user_bot.utils.UserState;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.List;

@Service
public final class InitCallbackHandler {
    private static final Logger LOGGER = Logger.getLogger(InitCallbackHandler.class);

    private final UserMenuVariables userMenuVariables;

    private final InitCategoryCallbackHandler initCategoryCallbackHandler;
    private final InitDistrictCallbackHandler initDistrictCallbackHandler;
    private final InitRoomCallbackHandler initRoomCallbackHandler;
    private final InitBudgetCallbackHandler initBudgetCallbackHandler;
    private final InitMenuEndHandler initMenuEndHandler;

    @Autowired
    public InitCallbackHandler(UserMenuVariables userMenuVariables, InitCategoryCallbackHandler initCategoryCallbackHandler, InitDistrictCallbackHandler initDistrictCallbackHandler, InitRoomCallbackHandler initRoomCallbackHandler, InitBudgetCallbackHandler initBudgetCallbackHandler, InitMenuEndHandler initMenuEndHandler) {
        this.userMenuVariables = userMenuVariables;
        this.initCategoryCallbackHandler = initCategoryCallbackHandler;
        this.initDistrictCallbackHandler = initDistrictCallbackHandler;
        this.initRoomCallbackHandler = initRoomCallbackHandler;
        this.initBudgetCallbackHandler = initBudgetCallbackHandler;
        this.initMenuEndHandler = initMenuEndHandler;
    }

    public void handleCallback(List<BotApiMethod<?>> response, CallbackQuery callbackQuery, UserCache user) {
        String data = callbackQuery.getData();

        // Если ткнули в неактуальное меню инициализации и не идет процесс подбора квартир (не все квартиры прислали)
        boolean forbidden = user.getUserChoice().getMenuMessageId() != null &&
                !user.getUserChoice().getMenuMessageId().equals(callbackQuery.getMessage().getMessageId()) &&
                user.getBotUserState() != UserState.SENT_NOT_ALL && user.getBotUserState() != UserState.FLATS_MASSAGING;

        LOGGER.info("CALLBACK DATA: " + data + ". FORBIDDEN: " + forbidden);

        if (forbidden) {
            return;
        }

        // TODO: закомментил setMenuMessageId
        /*// Если нажали - запоминаем айди меню инициализации (в одно время может работать только одно)
        if (user.getUserChoice().getMenuMessageId() == null) {
            user.getUserChoice().setMenuMessageId(callbackQuery.getMessage().getMessageId());
        }*/

        // Если callback пришел от какой-то кнопки с меню инициализации категории
        if (data.startsWith(userMenuVariables.getMenuInitBtnCategoryCallbackPrefix())) {
            initCategoryCallbackHandler.handleCallback(response, callbackQuery, user);
        }

        // Если callback пришел от какой-то кнопки с меню инициализации комнат
        if (data.startsWith(userMenuVariables.getMenuInitRoomsBtnCallbackPrefix())) {
            initRoomCallbackHandler.handleCallback(response, callbackQuery, user);
        }

        // Если callback пришел от какой-то кнопки с меню инициализации районов
        if (data.startsWith(userMenuVariables.getMenuInitDistrictsBtnPrefixCallback())) {
            initDistrictCallbackHandler.handleCallback(response, callbackQuery, user);
        }

        // Если callback пришел от какой-то кнопки с меню инициализации бюджетов
        if (data.startsWith(userMenuVariables.getMenuInitBudgetBtnRangePrefixCallback())) {
            initBudgetCallbackHandler.handleCallback(response, callbackQuery, user);
        }

        // Если в initBudgetCallbackHandler изменили состояние на FIRST_INIT_END
        if (user.getBotUserState() == UserState.FIRST_INIT_END) {
            long time2 = System.currentTimeMillis();
            initMenuEndHandler.handleInitMenuEnd(response, user);
            LOGGER.info("Time handleInitMenuEnd: " + (System.currentTimeMillis() - time2));
        }
    }
}

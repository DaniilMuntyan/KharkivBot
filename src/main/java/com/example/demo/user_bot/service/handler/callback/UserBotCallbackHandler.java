package com.example.demo.user_bot.service.handler.callback;

import com.example.demo.common_part.constants.UserMenuVariables;
import com.example.demo.user_bot.cache.UserCache;
import com.example.demo.user_bot.service.entities.UserService;
import com.example.demo.user_bot.service.handler.callback.init.*;
import com.example.demo.user_bot.utils.UserState;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public final class UserBotCallbackHandler {
    private static final Logger LOGGER = Logger.getLogger(UserBotCallbackHandler.class);

    private final UserService userService;
    private final UserMenuVariables userMenuVariables;

    private final InitCategoryCallbackHandler initCategoryCallbackHandler;
    private final InitRoomCallbackHandler initRoomCallbackHandler;
    private final InitDistrictCallbackHandler initDistrictCallbackHandler;
    private final InitBudgetCallbackHandler initBudgetCallbackHandler;
    private final InitMenuEndHandler initMenuEndHandler;
    private final SeeOthersCallbackHandler seeOthersCallbackHandler;

    @Autowired
    public UserBotCallbackHandler(UserService userService, UserMenuVariables userMenuVariables, InitCategoryCallbackHandler initCategoryCallbackHandler, InitRoomCallbackHandler initRoomCallbackHandler, InitDistrictCallbackHandler initDistrictCallbackHandler, InitBudgetCallbackHandler initBudgetCallbackHandler, InitMenuEndHandler initMenuEndHandler, SeeOthersCallbackHandler seeOthersCallbackHandler) {
        this.userService = userService;
        this.userMenuVariables = userMenuVariables;
        this.initCategoryCallbackHandler = initCategoryCallbackHandler;
        this.initRoomCallbackHandler = initRoomCallbackHandler;
        this.initDistrictCallbackHandler = initDistrictCallbackHandler;
        this.initBudgetCallbackHandler = initBudgetCallbackHandler;
        this.initMenuEndHandler = initMenuEndHandler;
        this.seeOthersCallbackHandler = seeOthersCallbackHandler;
    }

    public List<BotApiMethod<?>> handleCallback(CallbackQuery callbackQuery) {
        Long chatId = callbackQuery.getMessage().getChatId();

        long time1 = System.currentTimeMillis();
        Optional<UserCache> user = userService.findUserInCache(chatId); // Ищу юзера в кэше

        List<BotApiMethod<?>> response = new ArrayList<>();

        String data = callbackQuery.getData();

        // Если ткнули в неактуальное меню или ткнули в неактуальное меню
        boolean forbidden = user.isEmpty() || (user.get().getUserChoice().getMenuMessageId() != null &&
                !user.get().getUserChoice().getMenuMessageId().equals(callbackQuery.getMessage().getMessageId()) &&
                user.get().getBotUserState() != UserState.SENT_NOT_ALL && user.get().getBotUserState() != UserState.FLATS_MASSAGING); // И не идет процесс подбора квартир (не все квартиры прислали)

        if (forbidden) { // Возвращаем пустой список АПИ методов
            return response;
        }

        // Если не получили Forbidden - отвечаем на callback, чтобы ушел кружек загрузки
        response.add(0, this.getAnswerCallback(callbackQuery));

        // Если нажали - запоминаем айди меню (в одно время может работать только одно)
        if (user.get().getUserChoice().getMenuMessageId() == null) {
            user.get().getUserChoice().setMenuMessageId(callbackQuery.getMessage().getMessageId());
        }

        // Если callback пришел от какой-то кнопки с меню категории
        if (data.startsWith(userMenuVariables.getMenuInitBtnCategoryCallbackPrefix())) {
            initCategoryCallbackHandler.handleCallback(response, callbackQuery, user.get());
        }

        // Если callback пришел от какой-то кнопки с меню комнат
        if (data.startsWith(userMenuVariables.getMenuInitRoomsBtnCallbackPrefix())) {
            initRoomCallbackHandler.handleCallback(response, callbackQuery, user.get());
        }

        // Если callback пришел от какой-то кнопки с меню районов
        if (data.startsWith(userMenuVariables.getMenuInitDistrictsBtnPrefixCallback())) {
            initDistrictCallbackHandler.handleCallback(response, callbackQuery, user.get());
        }

        // Если callback пришел от какой-то кнопки с меню бюджетов
        if (data.startsWith(userMenuVariables.getMenuInitBudgetBtnRangePrefixCallback())) {
            initBudgetCallbackHandler.handleCallback(response, callbackQuery, user.get());
        }

        // Если в initBudgetCallbackHandler изменили состояние на FIRST_INIT_END
        if (user.get().getBotUserState() == UserState.FIRST_INIT_END) {
            long time2 = System.currentTimeMillis();
            initMenuEndHandler.handleInitMenuEnd(response, user.get());
            LOGGER.info("Time handleInitMenuEnd: " + (System.currentTimeMillis() - time2));
        }

        // Если нажали на какую-то кнопку из меню "Показать еще" для квартир
        if (data.startsWith(userMenuVariables.getUserBotNotAllBtnCallbackPrefix())) {
            long time2 = System.currentTimeMillis();
            seeOthersCallbackHandler.handleCallback(response, callbackQuery, user.get());
            LOGGER.info("Time seeOthersCallbackHandler.handleCallback: " + (System.currentTimeMillis() - time2));
        }

        userService.saveUserCache(user.get()); // Сохраняю все изменения юзера
        LOGGER.info("Time handleCallback: " + (System.currentTimeMillis() - time1));
        return response;
    }

    private AnswerCallbackQuery getAnswerCallback(CallbackQuery callbackQuery) {
        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
        answerCallbackQuery.setCallbackQueryId(callbackQuery.getId());
        answerCallbackQuery.setShowAlert(false);
        return answerCallbackQuery;
    }
}

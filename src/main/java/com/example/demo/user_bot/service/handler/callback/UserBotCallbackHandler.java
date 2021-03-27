package com.example.demo.user_bot.service.handler.callback;

import com.example.demo.common_part.constants.UserMenuVariables;
import com.example.demo.user_bot.cache.UserCache;
import com.example.demo.user_bot.service.entities.UserService;
import com.example.demo.user_bot.service.handler.callback.flat.ConfirmSeeingCallbackHandler;
import com.example.demo.user_bot.service.handler.callback.flat.FlatButtonCallbackHandler;
import com.example.demo.user_bot.service.handler.callback.init.*;
import com.example.demo.user_bot.service.handler.callback.menu.Menu21CategoryCallbackHandler;
import com.example.demo.user_bot.service.handler.callback.see_others.SeeOthersOrEnoughCallbackHandler;
import com.example.demo.user_bot.utils.UserState;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public final class UserBotCallbackHandler {
    private static final Logger LOGGER = Logger.getLogger(UserBotCallbackHandler.class);

    private final UserService userService;
    private final UserMenuVariables userMenuVariables;

    private final InitCallbackHandler initCallbackHandler;
    private final Menu2CallbackHandler menu2CallbackHandler;

    private final InitCategoryCallbackHandler initCategoryCallbackHandler;
    private final InitRoomCallbackHandler initRoomCallbackHandler;
    private final InitDistrictCallbackHandler initDistrictCallbackHandler;
    private final InitBudgetCallbackHandler initBudgetCallbackHandler;
    private final InitMenuEndHandler initMenuEndHandler;
    private final SeeOthersOrEnoughCallbackHandler seeOthersOrEnoughCallbackHandler;

    private final Menu21CategoryCallbackHandler menu21CategoryCallbackHandler;

    private final FlatButtonCallbackHandler flatButtonCallbackHandler;
    private final ConfirmSeeingCallbackHandler confirmSeeingCallbackHandler;

    @Autowired
    public UserBotCallbackHandler(UserService userService, UserMenuVariables userMenuVariables, InitCallbackHandler initCallbackHandler, Menu2CallbackHandler menu2CallbackHandler, InitCategoryCallbackHandler initCategoryCallbackHandler, InitRoomCallbackHandler initRoomCallbackHandler, InitDistrictCallbackHandler initDistrictCallbackHandler, InitBudgetCallbackHandler initBudgetCallbackHandler, InitMenuEndHandler initMenuEndHandler, SeeOthersOrEnoughCallbackHandler seeOthersOrEnoughCallbackHandler, Menu21CategoryCallbackHandler menu21CategoryCallbackHandler, FlatButtonCallbackHandler flatButtonCallbackHandler, ConfirmSeeingCallbackHandler confirmSeeingCallbackHandler) {
        this.userService = userService;
        this.userMenuVariables = userMenuVariables;
        this.initCallbackHandler = initCallbackHandler;
        this.menu2CallbackHandler = menu2CallbackHandler;
        this.initCategoryCallbackHandler = initCategoryCallbackHandler;
        this.initRoomCallbackHandler = initRoomCallbackHandler;
        this.initDistrictCallbackHandler = initDistrictCallbackHandler;
        this.initBudgetCallbackHandler = initBudgetCallbackHandler;
        this.initMenuEndHandler = initMenuEndHandler;
        this.seeOthersOrEnoughCallbackHandler = seeOthersOrEnoughCallbackHandler;
        this.menu21CategoryCallbackHandler = menu21CategoryCallbackHandler;
        this.flatButtonCallbackHandler = flatButtonCallbackHandler;
        this.confirmSeeingCallbackHandler = confirmSeeingCallbackHandler;
    }

    public List<BotApiMethod<?>> handleCallback(CallbackQuery callbackQuery) {
        Long chatId = callbackQuery.getMessage().getChatId();

        Optional<UserCache> user = userService.findUserInCacheOrDb(chatId); // Ищу юзера в кэше

        List<BotApiMethod<?>> response = new ArrayList<>();

        String data = callbackQuery.getData();

        boolean forbidden = user.isEmpty();

        if (forbidden) { // Возвращаем пустой список АПИ методов
            return response;
        }

        // Если не получили Forbidden - отвечаем на callback, чтобы ушел кружек загрузки
        response.add(0, this.getAnswerCallback(callbackQuery));

        // Если нажали на какую-то кнопку с меню инициализации
        if (data.startsWith(userMenuVariables.getMenuInitBtnCallbackPrefix())) {
            this.initCallbackHandler.handleCallback(response, callbackQuery, user.get());
        }

        boolean seeOthers = data.startsWith(userMenuVariables.getUserBotNotAllBtnCallbackPrefix()) &&
                user.get().getBotUserState() == UserState.SENT_NOT_ALL;
        // Если нажали на какую-то кнопку из меню "Показать еще" для квартир в состоянии SENT_NOT_ALL
        if (seeOthers) {
            seeOthersOrEnoughCallbackHandler.handleCallback(response, callbackQuery, user.get());
        }

        // Если нажали на какую-то кнопку из подпунктов меню "Мои предпочтения"
        if (data.startsWith(userMenuVariables.getUserMyMenuCallbackPrefix())) {
            menu2CallbackHandler.handleCallback(response, callbackQuery, user.get());
        }

        // Если нажали на "Хочу посмотреть" под квартирой
        if (data.startsWith(userMenuVariables.getUserBotFlatMsgSeeCallbackPrefix())) {
            flatButtonCallbackHandler.handleCallback(response, callbackQuery, user.get());
        }

        // Если нажали на кнопку из меню подтверждения записи на просмотр
        if (data.startsWith(userMenuVariables.getUserConfirmSeeingCallbackPrefix())) {
            confirmSeeingCallbackHandler.handleCallback(response, callbackQuery, user.get());
        }

        user.get().setLastAction(new Date()); // Фиксируем последнее действие
        userService.saveUserCache(user.get()); // Сохраняю все изменения юзера

        return response;
    }

    private AnswerCallbackQuery getAnswerCallback(CallbackQuery callbackQuery) {
        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
        answerCallbackQuery.setCallbackQueryId(callbackQuery.getId());
        answerCallbackQuery.setShowAlert(false);
        return answerCallbackQuery;
    }
}

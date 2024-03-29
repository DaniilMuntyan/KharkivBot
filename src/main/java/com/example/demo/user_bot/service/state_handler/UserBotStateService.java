package com.example.demo.user_bot.service.state_handler;

import com.example.demo.common_part.constants.MessagesVariables;
import com.example.demo.user_bot.cache.UserCache;
import com.example.demo.user_bot.keyboards.KeyboardsRegistry;
import com.example.demo.user_bot.service.DeleteMessageService;
import com.example.demo.user_bot.service.entities.UserService;
import com.example.demo.user_bot.service.handler.message.MessageHandlerRegistry;
import com.example.demo.user_bot.service.handler.message.menu.BackToMenu1;
import com.example.demo.user_bot.utils.MenuSendMessage;
import com.example.demo.user_bot.utils.UserCommands;
import com.example.demo.user_bot.utils.UserState;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserBotStateService {
    private static final Logger LOGGER = Logger.getLogger(UserBotStateService.class);

    private final UserService userService;
    private final MessagesVariables messagesVariables;
    private final KeyboardsRegistry keyboardsRegistry;
    private final MessageHandlerRegistry messageHandlerRegistry;
    private final DeleteMessageService deleteMessageService;

    private final BackToMenu1 backToMenu1;

    @Autowired
    public UserBotStateService(UserService userService, MessagesVariables messagesVariables, KeyboardsRegistry keyboardsRegistry, MessageHandlerRegistry messageHandlerRegistry, DeleteMessageService deleteMessageService, BackToMenu1 backToMenu1) {
        this.userService = userService;
        this.messagesVariables = messagesVariables;
        this.keyboardsRegistry = keyboardsRegistry;
        this.messageHandlerRegistry = messageHandlerRegistry;
        this.deleteMessageService = deleteMessageService;
        this.backToMenu1 = backToMenu1;
    }

    public List<BotApiMethod<?>> processUserInput(Message message, UserCache user) {
        List<BotApiMethod<?>> answer = new ArrayList<>();

        // Порядок важен! Так как внутри методов меняется состояние юзера,
        // то возвращаясь сюда - можем зайти в следующий по порядку if

        if (message.hasText()) {
            LOGGER.info(message.getText() + " " + user.getBotUserState().toString());
        }

        if (user.getUserChoice().getMenuMessageId() != null) { // Если было открыто меню - удаляю
            answer.add(this.deleteMessageService.deleteApiMethod(user.getChatId(), user.getUserChoice().getMenuMessageId()));
            user.getUserChoice().setMenuMessageId(null); // Удалил меню
        }

        switch(user.getBotUserState()) {
            case MENU1: // Если пришло сообщение в состоянии Menu1 (скорее всего нажали кнопку)
                answer.addAll(this.messageHandlerRegistry.getMenu1MessageHandler().handleMessage(message, user));
                break;
            case MENU2: // Если пришло сообщение в состоянии Menu2 (скорее всего нажали кнопку)
            case MENU21: // Любое подменю с пункта "Мои предпочтения"
            case MENU22: // Любое подменю с пункта "Мои предпочтения"
            case MENU23: // Любое подменю с пункта "Мои предпочтения"
            case MENU24: // Любое подменю с пункта "Мои предпочтения"
                answer.addAll(this.messageHandlerRegistry.getMenu2MessageHandler().handleMessage(message, user));
                break;
            case MENU3: // Кнопка из меню "Настройки"
                answer.addAll(this.messageHandlerRegistry.getMenu3MessageHandler().handleMessage(message, user));
                break;
            case MENU32: // Кнопка из меню "Указать номер телефона"
                answer.addAll(this.messageHandlerRegistry.getMenu32MessageHandler().handleMessage(message, user));
                break;
            case FIRST_INIT_CATEGORY: // Меню инициализации, выбрать категорию
                this.processCategory(answer, message, user);
                break;
            case INIT: // Если прислали любое сообщение в начальном состоянии
                this.processInit(answer, message, user);
                break;
            default:
                answer.add(this.backToMenu1.dontUnderstand(user));
                break;
        }

        userService.saveUserCache(user); // Сохраняем измененные параметры администратора
        return answer;
    }

    private void processCategory(List<BotApiMethod<?>> answer, Message message, UserCache user) {
        // Если прислали не /start в состоянии FIRST_INIT_CATEGORY
        if (message.hasText() && !message.getText().equals(UserCommands.START)) {
            answer.add(this.deleteMessageService.deleteApiMethod(message));
            return;
        }

        SendMessage messageHi = new SendMessage(); // Приветственное сообщение
        messageHi.setChatId(message.getChatId().toString());
        messageHi.setText(messagesVariables.getUserFirstHi(user.getName(false)));

        MenuSendMessage messageCategory = new MenuSendMessage(); // Сообщение с меню инициализации №1 (категории)
        messageCategory.setChatId(message.getChatId().toString());
        messageCategory.setText(messagesVariables.getUserInitCategoryText());
        messageCategory.setReplyMarkup(keyboardsRegistry.getInitCategoryMenu().getKeyboard(user.getUserChoice()));
        messageCategory.setChangeMenuMessageId(true);

        answer.addAll(List.of(messageHi, messageCategory));
        user.setBotUserState(UserState.FIRST_INIT_CATEGORY); // Меняю состояние бота, теперь выбираем категорию
    }

    private void processInit(List<BotApiMethod<?>> answer, Message message, UserCache user) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText(messagesVariables.getUserHi(user.getName(false)));
        sendMessage.setReplyMarkup(keyboardsRegistry.getInitCategoryMenu().getKeyboard(user.getUserChoice()));

        answer.add(sendMessage);
    }
}
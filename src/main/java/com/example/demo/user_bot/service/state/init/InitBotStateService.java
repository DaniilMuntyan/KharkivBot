package com.example.demo.user_bot.service.state.init;

import com.example.demo.admin_bot.constants.MessagesVariables;
import com.example.demo.common_part.model.User;
import com.example.demo.user_bot.service.KeyboardsRegistryService;
import com.example.demo.user_bot.service.entities.UserService;
import com.example.demo.user_bot.utils.UserState;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.ArrayList;
import java.util.List;

@Service
public final class InitBotStateService {
    private static final Logger LOGGER = Logger.getLogger(InitBotStateService.class);

    private final KeyboardsRegistryService keyboardsRegistryService;
    private final MessagesVariables messagesVariables;
    private final UserService userService;

    // В некоторых методах возможен Exception, поэтому не всегда надо возвращаться в предыдущее состояние
    private boolean notBack;

    @Autowired
    public InitBotStateService(KeyboardsRegistryService keyboardsRegistryService, MessagesVariables messagesVariables, UserService userService) {
        this.keyboardsRegistryService = keyboardsRegistryService;
        this.messagesVariables = messagesVariables;
        this.userService = userService;
    }

    public List<BotApiMethod<?>> processUserFirstInput(Message message, User user) {
        List<BotApiMethod<?>> answer = new ArrayList<>();
        notBack = true;
        LOGGER.info(user.getBotUserState());

        // Если первый раз зашли
        if (user.getBotUserState() == UserState.FIRST_INIT_CATEGORY) {
            this.processFirstInitCategory(answer, message, user);
        }

        userService.saveUser(user); // Сохраняем измененные параметры администратора

        return answer;
    }

    private DeleteMessage deleteApiMethod(Message message) {
        return DeleteMessage.builder()
                .chatId(message.getChatId().toString())
                .messageId(message.getMessageId())
                .build();
    }

    private EditMessageText getEditKeyboard(Long chatId, Integer messageId, User admin) {
        // TODO: возврат в menu2

        /*NewFlatMenu newFlatMenu = new NewFlatMenu(admin.getAdminChoice());
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setMessageId(messageId);
        editMessageText.setChatId(chatId.toString());
        editMessageText.setReplyMarkup(newFlatMenu.getKeyboard());
        editMessageText.setText(admin.getAdminChoice().getIsRentFlat() ?
                adminMenuVariables.getAdminAddRentFlatText() : adminMenuVariables.getAdminAddBuyFlatText());
        this.notBack = false; // Поменяли клавиатуру - значит можем вернуться в предыдущее состояние поубликации квартиры
        return editMessageText;*/
        return null;
    }

    private void processFirstInitCategory(List<BotApiMethod<?>> answer, Message message, User user) {
        SendMessage messageHi = new SendMessage(); // Приветственное сообщение
        messageHi.setChatId(message.getChatId().toString());
        messageHi.setText(messagesVariables.getUserFirstHi(user.getName(false)));

        SendMessage messageCategory = new SendMessage(); // Сообщение с меню инициализации №1 (категории)
        messageCategory.setChatId(message.getChatId().toString());
        messageCategory.setText(messagesVariables.getUserInitCategoryText());
        messageCategory.setReplyMarkup(keyboardsRegistryService.getInitCategoryMenu().getKeyboard());

        answer.addAll(List.of(messageHi, messageCategory));

        user.setBotUserState(UserState.FIRST_INIT_CATEGORY); // Меняю состояние бота, теперь выбираем категорию
    }

}

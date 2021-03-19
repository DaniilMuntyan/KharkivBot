package com.example.demo.user_bot.service.handler.message.menu;

import com.example.demo.admin_bot.constants.MessagesVariables;
import com.example.demo.common_part.constants.UserMenuVariables;
import com.example.demo.common_part.model.BuyFlat;
import com.example.demo.common_part.model.RentFlat;
import com.example.demo.user_bot.cache.DataCache;
import com.example.demo.user_bot.cache.UserCache;
import com.example.demo.user_bot.keyboards.KeyboardsRegistry;
import com.example.demo.user_bot.service.publishing.FindFlatsService;
import com.example.demo.user_bot.service.publishing.SendFoundFlatsService;
import com.example.demo.user_bot.utils.UserState;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public final class Menu2MessageHandler {
    private static final Logger LOGGER = Logger.getLogger(Menu2MessageHandler.class);

    private final UserMenuVariables userMenuVariables;
    private final MessagesVariables messagesVariables;
    private final KeyboardsRegistry keyboardsRegistry;

    private final FindFlatsService findFlatsService;
    private final SendFoundFlatsService sendFoundFlatsService;

    private final DataCache dataCache;

    private final BackToMenu1 backToMenu1;

    @Autowired
    public Menu2MessageHandler(UserMenuVariables userMenuVariables, MessagesVariables messagesVariables, KeyboardsRegistry keyboardsRegistry, FindFlatsService findFlatsService, SendFoundFlatsService sendFoundFlatsService, DataCache dataCache, BackToMenu1 backToMenu1) {
        this.userMenuVariables = userMenuVariables;
        this.messagesVariables = messagesVariables;
        this.keyboardsRegistry = keyboardsRegistry;
        this.findFlatsService = findFlatsService;
        this.sendFoundFlatsService = sendFoundFlatsService;
        this.dataCache = dataCache;
        this.backToMenu1 = backToMenu1;
    }

    public List<BotApiMethod<?>> handleMessage(Message message, UserCache user) {
        String text = message.getText();
        Long chatId = message.getChatId();

        boolean dontUnderstand = true; // Не понимаю пользователя (пришло левое сообщение)

        List<BotApiMethod<?>> response = new ArrayList<>();

        LOGGER.info(text);

        if (text.equals(userMenuVariables.getMenu2BtnCategoryText())) { // Нажали "изменить категорию"
            dontUnderstand = false;
            SendMessage menu21Message = new SendMessage();
            menu21Message.setChatId(chatId.toString());
            menu21Message.setReplyMarkup(keyboardsRegistry.getMenu21().getKeyboard(user.getUserChoice()));
            menu21Message.setText(messagesVariables.getUserMenu21Text());
            response.add(menu21Message);

            user.setBotUserState(UserState.MENU21); // Переходим в состояние Menu2
            dataCache.markNotSaved(chatId);
        }

        if (text.equals(userMenuVariables.getMenu2BtnRoomsText())) { // Нажали "изменить кол-во комнат"
            dontUnderstand = false;
            SendMessage menu22Message = new SendMessage();
            menu22Message.setChatId(chatId.toString());
            menu22Message.setReplyMarkup(keyboardsRegistry.getMenu22().getKeyboard(user.getUserChoice()));
            menu22Message.setText(messagesVariables.getUserMenu22Text());
            response.add(menu22Message);

            user.setBotUserState(UserState.MENU22); // Переходим в состояние Menu22
            dataCache.markNotSaved(chatId);
        }

        if (text.equals(userMenuVariables.getMenu2BtnDistrictsText())) { // Нажали "выбрать районы"
            dontUnderstand = false;
            SendMessage menu23Message = new SendMessage();
            menu23Message.setChatId(chatId.toString());
            menu23Message.setReplyMarkup(keyboardsRegistry.getMenu23().getKeyboard(user.getUserChoice()));
            menu23Message.setText(messagesVariables.getUserMenu23Text());
            response.add(menu23Message);

            user.setBotUserState(UserState.MENU23); // Переходим в состояние Menu23
            dataCache.markNotSaved(chatId);
        }

        if (text.equals(userMenuVariables.getMenu2BtnBudgetText())) { // Нажали "изменить бюджет"
            dontUnderstand = false;
            SendMessage menu24Message = new SendMessage();
            menu24Message.setChatId(chatId.toString());
            menu24Message.setReplyMarkup(keyboardsRegistry.getMenu24().getKeyboard(user.getUserChoice()));
            menu24Message.setText(messagesVariables.getUserMenu24Text());
            response.add(menu24Message);

            user.setBotUserState(UserState.MENU24); // Переходим в состояние Menu24
            dataCache.markNotSaved(chatId);
        }

        if (text.equals(userMenuVariables.getMenu2BtnBackText())) { // Нажали "назад"
            dontUnderstand = false;
            response.add(this.backToMenu1.back(user));
            /*SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId.toString());
            sendMessage.enableMarkdown(true);
            sendMessage.setText(messagesVariables.getUserMenu1Text());
            sendMessage.setReplyMarkup(keyboardsRegistry.getMenu1().getKeyboard());
            response.add(sendMessage);

            user.setBotUserState(UserState.MENU1); // Возвращаемся обратно
            dataCache.markNotSaved(chatId); // Чтобы потом сохранить в базу*/
        }

        if (text.equals(userMenuVariables.getMenu2BtnSearchText())) { // Нажали "Обновить"
            dontUnderstand = false;
            this.sendFoundFlats(user, response); // Ищу и отправляю подходящие квартиры
        }

        if (dontUnderstand) { // Не понимаю юзера
            response.add(this.backToMenu1.dontUnderstand(user));
            /*SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId.toString());
            sendMessage.setText(messagesVariables.getUserDontUnderstandText());
            sendMessage.setReplyMarkup(keyboardsRegistry.getMenu1().getKeyboard());
            response.add(sendMessage);

            user.setBotUserState(UserState.MENU1); // Перешли в главное меню
            dataCache.markNotSaved(chatId); // Чтобы потом сохранить в базу*/
        }

        return response;
    }

    // TODO: возможно, нужно вынести это в сервис SendFoundFlatsService
    private void sendFoundFlats(UserCache user, List<BotApiMethod<?>> response) {
        if (user.getUserChoice().getIsRentFlat()) { // Если пользователь ищет квартиру под аренду
            List<RentFlat> notSentRentFlats = new ArrayList<>(); // Список неотправленных юзеру квартир
            Set<RentFlat> userChoiceFlats = new HashSet<>(); // Сэт квартир под выбор пользователя

            this.findFlatsService.findRentFlatsForUser(user, notSentRentFlats, userChoiceFlats); // Заполняю списки

            // Устанавливаю список неотправленных квартир юзеру
            this.dataCache.setNotSentRentFlats(notSentRentFlats, user);
            // Устанавливаю только что заполненный, новый сэт квартир под выбор пользователя, так как теперь он полностью другой
            this.dataCache.setUserChoiceRentFlats(userChoiceFlats, user);

            if (notSentRentFlats.size() == 0) { // Если не нашлось квартир
                response.add(this.flatsNotFoundMessage(user));
            } else { // Если подходящие квартиры есть
                this.sendFoundFlatsService.sendNotSentRentFlats(user);
            }
        } else { // Если пользователь ищет квартиру для покупки
            List<BuyFlat> notSentBuyFlats = new ArrayList<>(); // Список неотправленных юзеру квартир
            Set<BuyFlat> userChoiceFlats = new HashSet<>(); // Сэт квартир под выбор пользователя

            this.findFlatsService.findBuyFlatsForUser(user, notSentBuyFlats, userChoiceFlats); // Заполняю списки

            // Устанавливаю список неотправленных квартир юзеру
            this.dataCache.setNotSentBuyFlats(notSentBuyFlats, user);
            // Устанавливаю только что заполненный, новый сэт квартир под выбор пользователя, так как теперь он полностью другой
            this.dataCache.setUserChoiceBuyFlats(userChoiceFlats, user);

            if (userChoiceFlats.size() == 0) { // Если не нашлось квартир
                response.add(this.flatsNotFoundMessage(user));
            } else { // Если подходящие квартиры есть
                this.sendFoundFlatsService.sendNotSentBuyFlats(user);
            }
        }

        this.dataCache.saveUser(user); // Сохраняю изменения юзера
    }

    private SendMessage flatsNotFoundMessage(UserCache user) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(user.getChatId().toString());
        sendMessage.setText(messagesVariables.getUserSentNoFlatsText());
        sendMessage.setReplyMarkup(keyboardsRegistry.getMenu1().getKeyboard()); // Меню 1

        user.setBotUserState(UserState.MENU1); // Перешли в главное меню
        this.dataCache.markNotSaved(user.getChatId()); // Сохраняю в базу измененное состояние

        return sendMessage;
    }
}

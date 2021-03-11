package com.example.demo.user_bot.service;

import com.example.demo.common_part.constants.UserMenuVariables;
import com.example.demo.common_part.model.User;
import com.example.demo.common_part.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Service
public final class UserService {
    private final UserMenuVariables userMenuVariables;

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserMenuVariables userMenuVariables, UserRepository userRepository) {
        this.userMenuVariables = userMenuVariables;
        this.userRepository = userRepository;
    }

    public ReplyKeyboardMarkup getMenu1() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton(userMenuVariables.getMenu1BtnChoiceText()));
        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton(userMenuVariables.getMenu1BtnSettingsText()));

        keyboard.add(row1);
        keyboard.add(row2);

        replyKeyboardMarkup.setKeyboard(keyboard);

        return replyKeyboardMarkup;
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }
}

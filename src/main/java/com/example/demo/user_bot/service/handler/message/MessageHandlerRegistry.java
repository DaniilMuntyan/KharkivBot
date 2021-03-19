package com.example.demo.user_bot.service.handler.message;

import com.example.demo.user_bot.service.handler.message.menu.Menu1MessageHandler;
import com.example.demo.user_bot.service.handler.message.menu.Menu2MessageHandler;
import com.example.demo.user_bot.service.handler.message.menu.Menu32MessageHandler;
import com.example.demo.user_bot.service.handler.message.menu.Menu3MessageHandler;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Getter
public final class MessageHandlerRegistry {
    private final Menu1MessageHandler menu1MessageHandler;
    private final Menu2MessageHandler menu2MessageHandler;
    private final Menu3MessageHandler menu3MessageHandler;
    private final Menu32MessageHandler menu32MessageHandler;

    @Autowired
    public MessageHandlerRegistry(Menu1MessageHandler menu1MessageHandler, Menu2MessageHandler menu2MessageHandler, Menu3MessageHandler menu3MessageHandler, Menu32MessageHandler menu32MessageHandler) {
        this.menu1MessageHandler = menu1MessageHandler;
        this.menu2MessageHandler = menu2MessageHandler;
        this.menu3MessageHandler = menu3MessageHandler;
        this.menu32MessageHandler = menu32MessageHandler;
    }
}

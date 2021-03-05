package com.example.demo.admin_bot.constants;

import com.example.demo.admin_bot.utils.Emoji;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
@PropertySource(value="classpath:messages.properties", encoding = "UTF-8")
public class MessagesVariables {
    @Value("${admin.hello}")
    @Getter
    private String helloAdmin;

    @Value("${admin.hi}")
    @Getter
    private String adminHi;

    @Value("${admin.menu.forbidden}")
    @Getter
    private String adminMenuForbidden;
}

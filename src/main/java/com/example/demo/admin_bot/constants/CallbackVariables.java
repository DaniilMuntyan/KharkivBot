package com.example.demo.admin_bot.constants;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:callback.properties")
public class CallbackVariables {
    @Value("${admin.bot.addFlat}")
    @Getter
    private String addFlat;
}

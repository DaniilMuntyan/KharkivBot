package com.example.demo.common_part.constants;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Component;

@Component
@PropertySources({
        @PropertySource("classpath:program.properties"),
        @PropertySource("classpath:admin-menu.properties")
})
public final class ProgramVariables {
    @Value("${spring.taskExecutor.corePoolSize}")
    @Getter
    private String threadsCorePoolSize;

    @Value("${spring.taskExecutor.maxPoolSize}")
    @Getter
    private String threadsMaxPoolSize;

    @Value("${spring.taskExecutor.queueCapacity}")
    @Getter
    private String threadsQueueCapacity;

    @Value("${admin.bot.password}")
    @Getter
    private String adminPassword;

    @Value("${admin.telegram.channel}")
    @Getter
    private String telegramChannel;

    /*@Value("${delay.user.message}")
    @Getter
    private Integer delayUserMessage;

    @Value("${delay.user.api}")
    @Getter
    private Integer delayUserApi;*/

    @Value("${delay.channel.message}")
    @Getter
    private Integer delayChannelMessage;

}

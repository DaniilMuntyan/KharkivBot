package com.example.demo.constants;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Component;

@Component
@PropertySources({
        @PropertySource("classpath:program.properties"),
        @PropertySource("classpath:menu.properties")
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

}

package com.example.demo.common_part.config;

import com.example.demo.common_part.constants.ProgramVariables;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
public class AsyncConfig {
    private static final Logger LOGGER = Logger.getLogger(AsyncConfig.class);

    private final ProgramVariables programVariables;

    @Autowired
    public AsyncConfig(ProgramVariables programVariables) {
        LOGGER.info("AsyncConfig is creating...");
        this.programVariables = programVariables;
    }

    @Bean(name="taskExecutor")
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        int corePoolSize = Integer.parseInt(programVariables.getThreadsCorePoolSize());
        int maxPoolSize = Integer.parseInt(programVariables.getThreadsMaxPoolSize());
        int queueCapacity = Integer.parseInt(programVariables.getThreadsQueueCapacity());
        taskExecutor.setCorePoolSize(corePoolSize);
        taskExecutor.setMaxPoolSize(maxPoolSize);
        taskExecutor.setQueueCapacity(queueCapacity);
        taskExecutor.setThreadNamePrefix("user-thread-");
        LOGGER.info("Created TaskExecutor bean. CorePoolSize: " + corePoolSize + ". " +
                "MaxPoolSize: " + maxPoolSize + ". " + "QueueCapacity: " + queueCapacity);
        return taskExecutor;
    }

    @Bean(name="taskScheduler")
    public TaskScheduler threadPoolTaskScheduler(){
        ThreadPoolTaskScheduler threadPoolTaskScheduler
                = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(1);
        threadPoolTaskScheduler.setThreadNamePrefix("ThreadPoolTaskScheduler");
        LOGGER.info("Created ThreadPoolTaskScheduler bean. PoolSize: " + threadPoolTaskScheduler.getPoolSize());
        return threadPoolTaskScheduler;
    }
}

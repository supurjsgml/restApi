package com.app.config;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {
    
	@Bean
    Executor taskExecutor() {
        final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        
        executor.setCorePoolSize(2);                        //기본적으로 유지할 스레드 수
        executor.setMaxPoolSize(4);                         //동시에 실행 가능한 최대 스레드 수
        executor.setQueueCapacity(100);                     //큐에 쌓아둘 작업 개수
        executor.setThreadNamePrefix("AsyncTaskExecutor-"); //스레드 이름 prefix
        executor.initialize();
        
        return executor;
    }
    
}

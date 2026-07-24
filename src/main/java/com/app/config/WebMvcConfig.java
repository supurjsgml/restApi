package com.app.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.app.common.util.MessagesUtils;

@Configuration
//@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {

	@Value("${spring.task.execution.pool.core-size:8}")
	private int coreSize;

	@Value("${spring.task.execution.pool.max-size:50}")
	private int maxSize;

	@Value("${spring.task.execution.pool.queue-capacity:100}")
	private int queueCapacity;

	@Value("${spring.task.execution.pool.keep-alive:60s}")
	private Duration keepAlive;

	@Value("${spring.task.execution.thread-name-prefix:mvc-async-}")
	private String threadNamePrefix;

	@Bean
	public ThreadPoolTaskExecutor mvcTaskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(coreSize);
		executor.setMaxPoolSize(maxSize);
		executor.setQueueCapacity(queueCapacity);
		executor.setKeepAliveSeconds((int) keepAlive.getSeconds());
		executor.setThreadNamePrefix(threadNamePrefix);
		executor.initialize();
		return executor;
	}

	@Override
	public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
		configurer.setTaskExecutor(mvcTaskExecutor());
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/js/**").addResourceLocations("classpath:/static/js/");
		registry.addResourceHandler("/css/**").addResourceLocations("classpath:/static/css/");
		registry.addResourceHandler("/images/**").addResourceLocations("classpath:/static/images/");
//		registry.addResourceHandler("/**").addResourceLocations("classpath:/templates/html/");
    }
	
	@Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:/messages/messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setAlwaysUseMessageFormat(true);
        messageSource.setUseCodeAsDefaultMessage(true);
        messageSource.setFallbackToSystemLocale(true);
        return messageSource;
    }


    @Bean
    public MessagesUtils messagesUtility() {
        MessagesUtils messagesUtility = new MessagesUtils();
        messagesUtility.setMessageSourceAccessor(new MessageSourceAccessor(this.messageSource()));
        return messagesUtility;
    }
    
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//		        .allowedOrigins(
//		                  "https://vue-front-807ffc6b572e.herokuapp.com" 
//		                , "https://guney.kro.kr"
//		                , "chrome-extension://ilaafkabnbkdgaieedckdnfkmapocimc" //구글 확장 프로그램 운영
//		                , "chrome-extension://chjbcemdkiommdpeklplkbfpemefejcp" //구글 확장 프로그램 운영
//		            )
////                .allowedOrigins("http://localhost")
//                .allowedMethods("*"); //"GET", "POST", "PUT", "DELETE", "FETCH"
//    }
}
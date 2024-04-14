package com.app.common.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:/prop/redoc-${spring.profiles.active:dev}.properties")
public class CommonConstant {

	@Value("${FILE_PATH}")
	public String FILE_PATH;
	
}

package com.app.common.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:/prop/redoc-${spring.profiles.active:dev}.properties")
public class CommonConstants {

	@Value("${FILE_PATH}")
	public String FILE_PATH;
	
}

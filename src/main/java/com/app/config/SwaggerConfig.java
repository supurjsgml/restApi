package com.app.config;

import java.util.Arrays;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI(){
    	
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER).name("Authorization");
        SecurityRequirement securityRequirement = new SecurityRequirement().addList("bearerAuth");

        return new OpenAPI()
        		.info(new Info().title("RestApi").description("Swagger redocly 프로젝트").version("v1"))
                .components(new Components().addSecuritySchemes("bearerAuth", securityScheme))
                .security(Arrays.asList(securityRequirement));
    }
    
    
    @Bean
    public GroupedOpenApi apiDocVxSample() {
        return GroupedOpenApi.builder()
            .group("샘플 API")
            .displayName("샘플 (sample)")
            .pathsToMatch("/api/**")
            .build();
    }
    
}
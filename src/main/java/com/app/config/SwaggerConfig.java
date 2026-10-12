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
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER).name("Authorization");
        SecurityRequirement securityRequirement = new SecurityRequirement().addList("bearerAuth");

        Components components = new Components().addSecuritySchemes("bearerAuth", securityScheme);

        Server apiServer = new Server()
                .url("https://guney-api.kro.kr")
                .description("PRD API");
        Server localServer = new Server()
                .url("http://localhost:8080")
                .description("로컬 서버 (localhost:8080)");

        return new OpenAPI()
                .info(new Info().title("RestApi").description("Swagger redocly 프로젝트").version("v1"))
                .components(components)
                .security(Arrays.asList(securityRequirement))
                .servers(Arrays.asList(apiServer, localServer));
    }

    @Bean
    public GroupedOpenApi apiDocCommon() {
        return GroupedOpenApi.builder()
                .group("공통 API")
                .displayName("공통")
                .pathsToMatch("/com/**")
                .build();
    }

    @Bean
    public GroupedOpenApi apiDocSample() {
        return GroupedOpenApi.builder()
                .group("B_샘플 API")
                .displayName("샘플 (sample)")
                .pathsToMatch("/api/**")
                .build();
    }

}
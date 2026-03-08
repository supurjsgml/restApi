package com.app.common.constants;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "rest-api")
public class RestApiProperties {

    private Batch batch;

    @Getter
    @Setter
    public static class Batch {
        private String baseUrl;
    }
}
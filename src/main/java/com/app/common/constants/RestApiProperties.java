package com.app.common.constants;

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
    private Kakao kakao;

    @Getter
    @Setter
    public static class Batch {
        private String baseUrl;
    }

    @Getter
    @Setter
    public static class Kakao {
        private String v2;
        private Api api;
        private Auth auth;

        @Getter
        @Setter
        public static class Api {
            private String baseUrl;
            private String memo;
        }

        @Getter
        @Setter
        public static class Auth {
            private String baseUrl;
            private String token;
        }
    }
}
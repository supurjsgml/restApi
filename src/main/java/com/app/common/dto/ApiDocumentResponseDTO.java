package com.app.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiDocumentResponseDTO {

    /**
     * The type Success.
     *
     * @param <T> the type parameter
     */
    @Getter
    @Setter
    @ToString
    @Schema(name = "ApiBodyDocumentDTO.Success", description = "200")
    public static class Success<T> {

        private HeaderSuccess header;

        // @Schema(anyOf = { SampleListDTO.ResponseV2.class })
        @Schema(description = "data", nullable = true)
        private T data;

        private Object page;

        @Getter
        @Schema(name = "ApiBodyDocumentDTO.HeaderSuccess", description = "")
        private static class HeaderSuccess {

            @Schema(description = "고유번호", example = "37f956e0-90fc-4e25-ad75-32fd208f58e8")
            private String uuid;

            @Schema(description = "결과여부", example = "true")
            private Boolean result;

            @Schema(description = "메시지코드", example = "SUCCESS")
            private String code;

            @Schema(description = "응답메세지", example = "정상")
            private String message;
        }
    }


    /**
     * The type Error.
     */
    @Getter
    @Setter
    @ToString
    @Schema(name = "ApiBodyDocumentDTO.Error", description = "DEFAULT")
    public static class Error<T> {

        private HeaderError header;
        private T data;
        private Object page;

        @JsonIgnore
        private String code;

        public Error(String code) {
            this.code = code;
        }

        @Getter
        @Schema(name = "ApiBodyDocumentDTO.HeaderError", description = "")
        private static class HeaderError {

            @Schema(description = "고유번호", example = "37f956e0-90fc-4e25-ad75-32fd208f58e8")
            private String uuid;

            @Schema(description = "결과여부", example = "false")
            private Boolean result;

            @Schema(description = "메시지코드", example = "DEFAULT")
            private String code = this.getCode();

            @Schema(description = "응답메세지", example = "관리자에게 문의 해주세요")
            private String message;
        }
    }

    /**
     * The type Error 400.
     *
     * @param <T> the type parameter
     */
    @Getter
    @Setter
    @ToString
    @Schema(name = "ApiBodyDocumentDTO.Error400", description = "400")
    public static class Error400<T> {

        private HeaderError400 header;
        private T data;
        private Object page;

        @JsonIgnore
        private String code;

        public Error400(String code) {
            this.code = code;
        }

        @Getter
        @Schema(name = "ApiBodyDocumentDTO.HeaderError400", description = "Bad Request")
        private static class HeaderError400 {

            @Schema(description = "고유번호", example = "37f956e0-90fc-4e25-ad75-32fd208f58e8")
            private String uuid;

            @Schema(description = "결과여부", example = "false")
            private Boolean result;

            @Schema(description = "메시지코드", example = "BAD_REQUEST")
            private String code;

            @Schema(description = "응답메세지", example = "잘못된 요청입니다")
            private String message;
        }
    }

    /**
     * The type Error 401.
     *
     * @param <T> the type parameter
     */
    @Getter
    @Setter
    @ToString
    @Schema(name = "ApiBodyDocumentDTO.Error_401", description = "401")
    public static class Error401<T> {

        private HeaderError401 header;
        private T data;
        private Object page;

        @JsonIgnore
        private String code;

        public Error401(String code) {
            this.code = code;
        }

        @Getter
        @Schema(name = "ApiBodyDocumentDTO.HeaderError401", description = "Unauthorized")
        private static class HeaderError401 {

            @Schema(description = "고유번호", example = "37f956e0-90fc-4e25-ad75-32fd208f58e8")
            private String uuid;

            @Schema(description = "결과여부", example = "false")
            private Boolean result;

            @Schema(description = "메시지코드", example = "UNAUTHORIZED")
            private String code;

            @Schema(description = "응답메세지", example = "해당 리소스 권한이 없습니다")
            private String message;
        }
    }

    /**
     * The type Error 403.
     *
     * @param <T> the type parameter
     */
    @Getter
    @Setter
    @ToString
    @Schema(name = "ApiBodyDocumentDTO.Error_403", description = "403")
    public static class Error403<T> {

        private HeaderError403 header;
        private T data;
        private Object page;

        @JsonIgnore
        private String code;

        public Error403(String code) {
            this.code = code;
        }

        @Getter
        @Schema(name = "ApiBodyDocumentDTO.HeaderError403", description = "Forbidden")
        private static class HeaderError403 {

            @Schema(description = "고유번호", example = "37f956e0-90fc-4e25-ad75-32fd208f58e8")
            private String uuid;

            @Schema(description = "결과여부", example = "false")
            private Boolean result;

            @Schema(description = "메시지코드", example = "FORBIDDEN")
            private String code;

            @Schema(description = "응답메세지", example = "해당 리소스에 대한 비인가 요청입니다")
            private String message;
        }
    }

    /**
     * The type Error 404.
     *
     * @param <T> the type parameter
     */
    @Getter
    @Setter
    @ToString
    @Schema(name = "ApiBodyDocumentDTO.Error_404", description = "404")
    public static class Error404<T> {

        private HeaderError404 header;
        private T data;
        private Object page;

        @JsonIgnore
        private String code;

        public Error404(String code) {
            this.code = code;
        }

        @Getter
        @Schema(name = "ApiBodyDocumentDTO.HeaderError404", description = "Not Found")
        private static class HeaderError404 {

            @Schema(description = "고유번호", example = "37f956e0-90fc-4e25-ad75-32fd208f58e8")
            private String uuid;

            @Schema(description = "결과여부", example = "false")
            private Boolean result;

            @Schema(description = "메시지코드", example = "NOT_FOUND")
            private String code;

            @Schema(description = "응답메세지", example = "요청한 리소스를 찾을 수 없습니다")
            private String message;
        }
    }

    /**
     * The type Error 405.
     *
     * @param <T> the type parameter
     */
    @Getter
    @Setter
    @ToString
    @Schema(name = "ApiBodyDocumentDTO.Error_405", description = "405")
    public static class Error405<T> {

        private HeaderError401 header;
        private T data;
        private Object page;

        @JsonIgnore
        private String code;

        public Error405(String code) {
            this.code = code;
        }

        @Getter
        @Schema(name = "ApiBodyDocumentDTO.HeaderError405", description = "Method Not Allowed")
        private static class HeaderError401 {

            @Schema(description = "고유번호", example = "37f956e0-90fc-4e25-ad75-32fd208f58e8")
            private String uuid;

            @Schema(description = "결과여부", example = "false")
            private Boolean result;

            @Schema(description = "메시지코드", example = "METHOD_NOT_ALLOWED")
            private String code;

            @Schema(description = "응답메세지", example = "허용되지 않는 메소드입니다")
            private String message;
        }
    }

    /**
     * The type Error 415.
     *
     * @param <T> the type parameter
     */
    @Getter
    @Setter
    @ToString
    @Schema(name = "ApiBodyDocumentDTO.Error_415", description = "415")
    public static class Error415<T> {

        private HeaderError401 header;
        private T data;
        private Object page;

        @JsonIgnore
        private String code;

        public Error415(String code) {
            this.code = code;
        }

        @Getter
        @Schema(name = "ApiBodyDocumentDTO.HeaderError415", description = "Unsupported Media Type")
        private static class HeaderError401 {

            @Schema(description = "고유번호", example = "37f956e0-90fc-4e25-ad75-32fd208f58e8")
            private String uuid;

            @Schema(description = "결과여부", example = "false")
            private Boolean result;

            @Schema(description = "메시지코드", example = "UNSUPPORTED_MEDIA_TYPE")
            private String code;

            @Schema(description = "응답메세지", example = "지원되지 않는 미디어 유형")
            private String message;
        }
    }

    /**
     * The type Error 500.
     *
     * @param <T> the type parameter
     */
    @Getter
    @Setter
    @ToString
    @Schema(name = "ApiBodyDocumentDTO.Error500", description = "500")
    public static class Error500<T> {

        private HeaderError500 header;
        private T data;
        private Object page;

        @JsonIgnore
        private String code;

        public Error500(String code) {
            this.code = code;
        }

        @Getter
        @Schema(name = "ApiBodyDocumentDTO.HeaderError500", description = "Internal Server Error")
        private static class HeaderError500 {

            @Schema(description = "고유번호", example = "37f956e0-90fc-4e25-ad75-32fd208f58e8")
            private String uuid;

            @Schema(description = "결과여부", example = "false")
            private Boolean result;

            @Schema(description = "메시지코드", example = "INTERNAL_SERVER_ERROR")
            private String code;

            @Schema(description = "응답메세지", example = "내부 서버 오류입니다")
            private String message;
        }
    }
}

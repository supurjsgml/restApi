package com.app.common.dto.res;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ApiDataResponse<T> extends HttpResponse {

    private T data;

    @Builder
    public ApiDataResponse (T data, String resultCode, String resultMessage) {
        this.data = data;
        this.setResultCode(resultCode);
        this.setResultMessage(resultMessage);
    }

    public ApiDataResponse <T> ok(T data) {
        return ApiDataResponse.<T>builder()
                .resultCode(this.getResultCode())
                .resultMessage(this.getResultMessage())
                .data(data)
                .build();
    }

    public ApiDataResponse <T> ok(T data, String resultCode, String resultMessage) {
        return ApiDataResponse.<T>builder()
                .resultCode(resultCode)
                .resultMessage(resultMessage)
                .data(data)
                .build();
    }

    public ApiDataResponse <T> fail() {
        Error500 error = new Error500();
        return ApiDataResponse.<T>builder()
                .resultCode(error.getResultCode())
                .resultMessage(error.getResultMessage())
                .build();
    }

    public ApiDataResponse <T> fail(String resultCode, String resultMessage) {
        return ApiDataResponse.<T>builder()
                .resultCode(resultCode)
                .resultMessage(resultMessage)
                .build();
    }

    public ApiDataResponse <T> validationFail(String resultMessage) {
        Error422 error = new Error422();
        return ApiDataResponse.<T>builder()
                .resultCode(error.getResultCode())
                .resultMessage(resultMessage)
                .build();
    }

    public ApiDataResponse <T> validationFail(String resultCode, String resultMessage) {
        return ApiDataResponse.<T>builder()
                .resultCode(resultCode)
                .resultMessage(resultMessage)
                .build();
    }
    
    public ApiDataResponse <T> validationFail(String resultMessage, T data) {
        Error422 error = new Error422();
        return ApiDataResponse.<T>builder()
                .resultCode(error.getResultCode())
                .resultMessage(resultMessage)
                .data(data)
                .build();
    }

}

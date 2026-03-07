package com.app.common.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class HttpResponse {

    @Schema(description = "메시지코드", example = "200")
    private String resultCode = String.valueOf(HttpStatus.OK.value());

    @Schema(description = "응답메세지", example = "Success")
    private String resultMessage = "Success";

    @Getter
    @Schema(name = "Error400", description = "Bad Request")
    public static class Error400 {

        @Schema(description = "메시지코드", example = "400")
        private String resultCode = String.valueOf(HttpStatus.BAD_REQUEST.value());

        @Schema(description = "응답메세지", example = "Bad Request")
        private String resultMessage = HttpStatus.BAD_REQUEST.getReasonPhrase();
    }

    @Getter
    @Schema(name = "Error500", description = "Bad Request")
    public static class Error500 {

        @Schema(description = "메시지코드", example = "500")
        private String resultCode = String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value());

        @Schema(description = "응답메세지", example = "Internal Server Error")
        private String resultMessage = HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase();
    }

    @Getter
    @Schema(name = "Error422", description = "Unprocessable Entity")
    public static class Error422 {

        @Schema(description = "메시지코드", example = "422")
        private String resultCode = String.valueOf(HttpStatus.UNPROCESSABLE_ENTITY.value());

        @Schema(description = "응답메세지", example = "Unprocessable Entity")
        private String resultMessage = HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase();
    }

}

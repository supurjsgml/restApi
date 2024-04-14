package com.app.common.core.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.app.common.dto.ApiDocumentResponseDTO;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

/**
 * <p>Controller level 에서 Swagger 의 응답 형태를 구성할 경우 선언한다.</p>
 * 
 * @user : guney
 * @date : 2024. 4. 14.
 * @since 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
@ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "API 정상 호출", useReturnTypeSchema = true, content = @Content(schema = @Schema(implementation = ApiDocumentResponseDTO.Success.class))),
    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ApiDocumentResponseDTO.Error400.class))),
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiDocumentResponseDTO.Error401.class))),
    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ApiDocumentResponseDTO.Error403.class))),
    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ApiDocumentResponseDTO.Error404.class))),
    @ApiResponse(responseCode = "405", description = "Method Not Allowed", content = @Content(schema = @Schema(implementation = ApiDocumentResponseDTO.Error405.class))),
    @ApiResponse(responseCode = "415", description = "Unsupported Media Type", content = @Content(schema = @Schema(implementation = ApiDocumentResponseDTO.Error415.class))),
    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ApiDocumentResponseDTO.Error500.class)))
})
public @interface ApiDocumentResponse {
}

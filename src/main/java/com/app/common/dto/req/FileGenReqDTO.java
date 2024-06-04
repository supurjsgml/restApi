package com.app.common.dto.req;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * FileGenReqDTO
 * 
 * @user  : guney
 * @date  : 2024. 4. 28.
 * @since : 1.0
 */
@Getter
@Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Schema(name = "FileGenReqDTO", description = "파일생성 DTO")
public class FileGenReqDTO {
	
	@Schema(description = "파일생성경로", example = "")
	private String filePath;
	
	@Schema(description = "파일명", example = "")
	private String fileNm;
	
	@Schema(description = "메소드명", example = "")
	private String methodNm = "methodNm";
	
	@NotNull(message = "{BAD_REQUEST}")
	@Schema(description = "dto 문자열", example = "")
	private String camelStr;
	
}

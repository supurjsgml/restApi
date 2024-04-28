package com.app.common.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
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
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class FileGenReqDTO {
	
	@Schema(description = "dto문자열", example = "")
	private String camelStr;
	
	@Schema(description = "파일명", example = "")
	private String fileNm;
	
}

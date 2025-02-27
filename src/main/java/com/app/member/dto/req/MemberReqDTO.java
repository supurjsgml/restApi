package com.app.member.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author guney
 * @date 2024. 3. 18.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Schema(name = "MemberReqDTO", description = "MemberReqDTO")
public class MemberReqDTO {
    
    @Schema(description = "아이디", example = "id")
    private String id;
    
    @Schema(description = "비밀번호", example = "pw")
    private String pw;
    
    @Schema(description = "토큰", example = "token", hidden = true)
    private String token;
    
}


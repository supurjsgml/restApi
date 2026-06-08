package com.app.internal.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Schema(name = "VisitorHitReqDTO", description = "방문자수 증가 요청 DTO")
public class VisitorHitReqDTO {

    @Schema(description = "페이지 이름", example = "Main")
    private String pageName;

    @JsonProperty("isNewSession")
    @Schema(description = "신규 세션 여부", example = "false")
    private boolean isNewSession;
}

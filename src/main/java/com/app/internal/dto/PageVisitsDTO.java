package com.app.internal.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@Schema(name = "PageVisitsDTO", description = "페이지별 누적 방문수 데이터")
public class PageVisitsDTO {

    @JsonProperty("Main")
    @Schema(description = "메인 페이지 방문수", example = "100")
    private int main;

    @JsonProperty("Camel")
    @Schema(description = "Camel 페이지 방문수", example = "50")
    private int camel;

    @JsonProperty("Translate")
    @Schema(description = "Translate 페이지 방문수", example = "30")
    private int translate;

    @JsonProperty("Diff")
    @Schema(description = "Diff 페이지 방문수", example = "40")
    private int diff;

    @JsonProperty("Google")
    @Schema(description = "Google 페이지 방문수", example = "20")
    private int google;
}

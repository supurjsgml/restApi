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

    @JsonProperty("Grafana")
    @Schema(description = "Grafana 페이지 방문수", example = "80")
    private int grafana;

    @JsonProperty("Google")
    @Schema(description = "Google 페이지 방문수", example = "20")
    private int google;

    @JsonProperty("Stats")
    @Schema(description = "Stats 모달 페이지 방문수", example = "10")
    private int stats;
}

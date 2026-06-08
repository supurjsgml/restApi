package com.app.internal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
@Schema(name = "DashboardStatsDTO", description = "대시보드 미니 통계 데이터")
public class DashboardStatsDTO {
    @Schema(description = "라벨", example = "주간 방문자 (1주)")
    private String label;

    @Schema(description = "총합 값", example = "48,250")
    private String value;

    @Schema(description = "설명", example = "실시간 동적 연동 중")
    private String description;

    @Schema(description = "트렌드 변동율", example = "15.4%")
    private String trend;

    @Schema(description = "트렌드 방향 (up/down)", example = "up")
    private String trendDirection;

    @Schema(description = "스파크라인 차트 수치 리스트")
    private List<Integer> sparklineValues;

    @Schema(description = "요일/일자 라벨 리스트")
    private List<String> days;
}

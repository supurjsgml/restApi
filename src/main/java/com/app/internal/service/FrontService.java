package com.app.internal.service;

import com.app.internal.dto.DashboardStatsDTO;
import com.app.internal.dto.PageVisitsDTO;

public interface FrontService {
    /**
     * 대시보드 미니 통계 정보 조회 (최근 7일 방문자 수)
     */
    DashboardStatsDTO getDashboardStats();

    /**
     * 페이지 방문자 수 및 신규 세션 여부에 따른 전체 방문자 수 증가
     */
    void incrementVisitorCount(String pageName, boolean isNewSession);

    /**
     * 페이지별 누적 방문자 수 목록 조회
     */
    PageVisitsDTO getPageVisits();
}

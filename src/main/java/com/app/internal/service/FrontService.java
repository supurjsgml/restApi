package com.app.internal.service;

import com.app.internal.dto.DashboardStatsDTO;

public interface FrontService {
    /**
     * 대시보드 미니 통계 정보 조회 (최근 7일 방문자 수)
     */
    DashboardStatsDTO getDashboardStats();

    /**
     * 오늘 방문자 수 카운트 증가
     */
    void incrementVisitorCount();
}

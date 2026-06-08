package com.app.internal.service.serviceimpl;

import com.app.internal.dto.DashboardStatsDTO;
import com.app.internal.dto.PageVisitsDTO;
import com.app.internal.service.FrontService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class FrontServiceImpl implements FrontService {

    private final StringRedisTemplate redisTemplate;
    private static final String DAILY_KEY_PREFIX = "visitor_count:";
    private static final String PAGE_VISITS_KEY = "page_visits";

    @Override
    public void incrementVisitorCount(String pageName, boolean isNewSession) {
        try {
            // 1. 페이지별 누적 방문수 증가 (항상 실행)
            if (pageName != null && !pageName.trim().isEmpty()) {
                redisTemplate.opsForHash().increment(PAGE_VISITS_KEY, pageName, 1L);
                log.info("페이지 뷰 증가: {} +1", pageName);
            }

            // 2. 신규 세션일 경우 일자별 전체 방문수 증가
            if (isNewSession) {
                String todayStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                String dailyKey = DAILY_KEY_PREFIX + todayStr;
                Long current = redisTemplate.opsForValue().increment(dailyKey, 1L);
                log.info("신규 세션 - 오늘 전체 방문자 수 증가: {} -> {}", todayStr, current);
            }
        } catch (Exception e) {
            log.error("Redis 방문자 카운트 증가 중 오류 발생: {}", e.getMessage());
        }
    }

    @Override
    public PageVisitsDTO getPageVisits() {
        int main = 0;
        int camel = 0;
        int grafana = 0;
        int google = 0;
        int statsVal = 0;

        try {
            Map<Object, Object> entries = redisTemplate.opsForHash().entries(PAGE_VISITS_KEY);
            main = parseCount(entries.get("Main"));
            camel = parseCount(entries.get("Camel"));
            grafana = parseCount(entries.get("Grafana"));
            google = parseCount(entries.get("Google"));
            statsVal = parseCount(entries.get("Stats"));
        } catch (Exception e) {
            log.error("Redis 페이지 방문자 조회 중 오류 발생: {}", e.getMessage());
        }

        return PageVisitsDTO.builder()
                .main(main)
                .camel(camel)
                .grafana(grafana)
                .google(google)
                .stats(statsVal)
                .build();
    }

    private int parseCount(Object value) {
        if (value == null) return 0;
        try {
            return Integer.parseInt((String) value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    @Override
    public DashboardStatsDTO getDashboardStats() {
        List<String> days = new ArrayList<>();
        List<Integer> values = new ArrayList<>();
        int total = 0;

        LocalDate today = LocalDate.now();
        DateTimeFormatter keyFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("dd");

        // 최근 7일치 키 목록 생성
        List<String> keys = new ArrayList<>();
        for (int i = 6; i >= 0; i--) {
            keys.add(DAILY_KEY_PREFIX + today.minusDays(i).format(keyFormatter));
        }

        List<String> rawValues = null;
        try {
            // Redis multiGet으로 7일치 데이터를 한 번의 네트워크 요청으로 한꺼번에 조회
            rawValues = redisTemplate.opsForValue().multiGet(keys);
        } catch (Exception e) {
            log.error("Redis 방문자 데이터 멀티조회 중 오류 발생: {}", e.getMessage());
        }

        for (int i = 0; i < 7; i++) {
            LocalDate date = today.minusDays(6 - i);
            
            // UI 표시용 날짜 설정
            if (6 - i == 0) {
                days.add("오늘");
            } else {
                days.add(date.format(displayFormatter));
            }

            int count = 0;
            if (rawValues != null && rawValues.get(i) != null) {
                try {
                    count = Integer.parseInt(rawValues.get(i));
                } catch (NumberFormatException ignored) {}
            }
            values.add(count);
            total += count;
        }

        // 트렌드 계산 (어제 대비 오늘 방문자 증감 비율)
        int todayCount = values.get(values.size() - 1);
        int yesterdayCount = values.get(values.size() - 2);
        String trend = "0%";
        String trendDirection = "up";
        if (yesterdayCount > 0) {
            double percent = ((double)(todayCount - yesterdayCount) / yesterdayCount) * 100;
            trend = String.format("%.1f%%", Math.abs(percent));
            trendDirection = percent >= 0 ? "up" : "down";
        } else if (todayCount > 0) {
            trend = "100%";
            trendDirection = "up";
        }

        return DashboardStatsDTO.builder()
                .label("주간 방문자 (1주)")
                .value(String.format("%,d", total))
//                .description("실시간 업스태시 서버리스 Redis 연동 중")
                .trend(trend)
                .trendDirection(trendDirection)
                .sparklineValues(values)
                .days(days)
                .build();
    }
}

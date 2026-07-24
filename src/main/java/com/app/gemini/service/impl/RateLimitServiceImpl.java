package com.app.gemini.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Service;

import com.app.gemini.service.RateLimitService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class RateLimitServiceImpl implements RateLimitService {

    private final ReactiveStringRedisTemplate reactiveStringRedisTemplate;

    @Value("${app.admin-emails:}")
    private List<String> adminEmails;

    @Value("${app.daily-limit:50}")
    private int dailyLimit;

    @Override
    public Mono<Boolean> checkLimit(String email, String googleSubId) {
        // 관리자 검사 (이메일 기준)
        if (email != null && adminEmails != null && adminEmails.contains(email)) {
            log.info("관리자 요청 허가");
            return Mono.just(true);
        }

        if (googleSubId == null || googleSubId.isEmpty()) {
            log.warn("식별가능한 구글 사용자 ID가 존재하지 않아 요청을 거부합니다.");
            return Mono.just(false);
        }

        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String redisKey = "rate:limit:" + today + ":" + googleSubId;

        // 오늘 자정까지 남은 시간 계산 (TTL 설정용)
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tomorrowMidnight = LocalDate.now().plusDays(1).atStartOfDay();
        Duration duration = Duration.between(now, tomorrowMidnight);
        long secondsRemaining = duration.getSeconds();

        return reactiveStringRedisTemplate.opsForValue().get(redisKey)
                .defaultIfEmpty("0")
                .flatMap(countStr -> {
                    int currentCount = Integer.parseInt(countStr);
                    if (currentCount >= dailyLimit) {
                        log.warn("사용자 일일 사용 한도 초과: email={}, sub={}, count={}", email, googleSubId, currentCount);
                        return Mono.just(false);
                    }

                    // 호출 횟수 증가 처리
                    return reactiveStringRedisTemplate.opsForValue().increment(redisKey)
                            .flatMap(newVal -> {
                                if (newVal == 1) {
                                    return reactiveStringRedisTemplate.expire(redisKey, Duration.ofSeconds(secondsRemaining))
                                            .map(expireSuccess -> true);
                                }
                                return Mono.just(true);
                            });
                });
    }
}

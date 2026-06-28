package com.app.common.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/lightsail")
@RequiredArgsConstructor
@Slf4j
public class LightsailController {

    @GetMapping("/restart")
    public HttpStatus restartScheduler() {
        log.info("라이트세일 PM2 프로세스(scheduler-job) 재시작 요청 수신");
        try {
            // 리눅스 환경에서 pm2 restart 명령을 자식 프로세스로 구동
            String[] cmd = {"/bin/sh", "-c", "pm2 restart scheduler-job"};
            Runtime.getRuntime().exec(cmd);
            log.info("PM2 재시작 명령 전송 완료");
        } catch (Exception e) {
            log.error("PM2 프로세스 재시작 실패: {}", e.getMessage());
        }
        return HttpStatus.OK;
    }
}

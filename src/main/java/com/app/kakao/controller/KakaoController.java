package com.app.kakao.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.common.dto.ApiBodyDTO;
import com.app.common.util.ApiResUtil;
import com.app.kakao.dto.KakaoRefreshReqDTO;
import com.app.kakao.dto.KakaoSendReqDTO;
import com.app.kakao.dto.KakaoTokenResDTO;
import com.app.kakao.service.KakaoService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/kakao")
public class KakaoController {

    private final KakaoService kakaoService;

    /**
     * Scheduler-Job → 카카오 토큰 갱신 요청
     */
    @PostMapping(value = "/refresh-token", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ApiBodyDTO.Response<KakaoTokenResDTO>> refreshToken(@RequestBody KakaoRefreshReqDTO req) {
        log.info("외부 카카오 토큰 갱신 요청 수신 (Scheduler-Job)");
        return kakaoService.refreshTokenExternal(req.getRefreshToken())
                .map(ApiResUtil::success);
    }

    /**
     * Scheduler-Job → 카카오 메시지 전송 요청
     */
    @PostMapping(value = "/send", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ApiBodyDTO.Response<KakaoTokenResDTO>> sendMessage(@RequestBody KakaoSendReqDTO req) {
        log.info("외부 카카오 메시지 전송 요청 수신 (Scheduler-Job): {}", req.getMsg());
        return kakaoService.sendKakaoExternal(req.getMsg(), req.getRefreshToken())
                .map(ApiResUtil::success);
    }

}

package com.app.kakao.service;

import com.app.kakao.dto.KakaoTokenResDTO;

import reactor.core.publisher.Mono;

public interface KakaoService {

    /**
     * 카카오 메시지 전송 (내부 호출용)
     */
    void sendKakao(String msg);

    /**
     * 카카오 토큰 갱신 (Redis 기반 토큰 관리)
     */
    Mono<KakaoTokenResDTO> refreshKakaoToken();

    /**
     * 카카오 메시지 전송 (외부 호출용)
     */
    Mono<KakaoTokenResDTO> sendKakaoExternal(String msg);

}


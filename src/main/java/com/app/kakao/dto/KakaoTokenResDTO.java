package com.app.kakao.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class KakaoTokenResDTO {

    private String accessToken;

    private String newRefreshToken;

    @Builder
    public KakaoTokenResDTO(String accessToken, String newRefreshToken) {
        this.accessToken = accessToken;
        this.newRefreshToken = newRefreshToken;
    }
}

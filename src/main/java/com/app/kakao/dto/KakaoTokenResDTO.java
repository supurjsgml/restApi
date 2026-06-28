package com.app.kakao.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

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

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String newRefreshToken;

    @Builder
    public KakaoTokenResDTO(String accessToken, String newRefreshToken) {
        this.accessToken = accessToken;
        this.newRefreshToken = newRefreshToken;
    }
}

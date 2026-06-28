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
public class KakaoSendReqDTO {

    private String msg;

    private String refreshToken;
}

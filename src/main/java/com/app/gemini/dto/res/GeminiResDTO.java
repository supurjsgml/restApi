package com.app.gemini.dto.res;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class GeminiResDTO {

    private String result;

    @Builder
    public GeminiResDTO(String result) {
        this.result = result;
    }
}

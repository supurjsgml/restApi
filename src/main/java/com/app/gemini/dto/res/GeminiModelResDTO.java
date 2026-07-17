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
public class GeminiModelResDTO {
    private String id;
    private String name;
    private String desc;

    @Builder
    public GeminiModelResDTO(String id, String name, String desc) {
        this.id = id;
        this.name = name;
        this.desc = desc;
    }
}

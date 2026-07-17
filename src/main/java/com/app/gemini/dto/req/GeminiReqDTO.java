package com.app.gemini.dto.req;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GeminiReqDTO {
    private String text;
    private String prompt;
    private String model;
}

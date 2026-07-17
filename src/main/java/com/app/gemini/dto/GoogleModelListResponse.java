package com.app.gemini.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GoogleModelListResponse {
    private List<GoogleModel> models;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GoogleModel {
        private String name;
        private String version;
        private String displayName;
        private String description;
        private List<String> supportedGenerationMethods;
    }
}

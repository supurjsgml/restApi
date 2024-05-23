package com.app.common.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PackageUtil {

    public static String createPackage(String packPath, String fileNm, String dtoNm) {
        String service = CommonUtil.decapitalize(fileNm).concat("Service");
        String lowUperDtoNm = CommonUtil.decapitalize(dtoNm);
        
        return String.format(
            """
            package %s.controller;

            import org.springframework.http.MediaType;
            import org.springframework.web.bind.annotation.PostMapping;
            import org.springframework.web.bind.annotation.RequestBody;
            import org.springframework.web.bind.annotation.RequestMapping;
            import org.springframework.web.bind.annotation.RestController;
            
            import %s.dto.%s;
            import %s.service.%sService;
            
            import io.swagger.v3.oas.annotations.Operation;
            import io.swagger.v3.oas.annotations.tags.Tag;
            import lombok.RequiredArgsConstructor;
            import lombok.extern.slf4j.Slf4j;
            
            @Slf4j
            @Tag(name = "", description = "")
            @RequiredArgsConstructor
            @RestController
            @RequestMapping(value = "/")
            public class %sController {
            
                private final %sService %s;
                
                @Operation(summary = "", description = "")
                @PostMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
                public void name(@RequestBody %s %s){
                    %s.fileDownload(%s);
                }
            
            }
            """, packPath, packPath, dtoNm, packPath, fileNm, fileNm, fileNm, service, dtoNm, lowUperDtoNm, service, lowUperDtoNm);
    }
    
}

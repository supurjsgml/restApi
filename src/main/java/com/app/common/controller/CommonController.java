package com.app.common.controller;

import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.common.core.RequestLoggingAspect;
import com.app.common.core.annotations.ApiDocumentResponse;
import com.app.common.dto.ApiDocumentResponseDTO.Success;
import com.app.common.dto.ApiDocumentResponseDTO.Success.HeaderSuccess;
import com.app.common.dto.req.FileGenReqDTO;
import com.app.common.exception.ValidException;
import com.app.common.service.CommonService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Tag(name = "공통", description = "공통 유틸 이랄까")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/com")
public class CommonController {

	private final CommonService commonService;
	
	@ApiDocumentResponse
    @Operation(summary = "파일생성", description = "File Generator")
	@PostMapping(value = "/genFile", produces = MediaType.APPLICATION_JSON_VALUE)
    public Success<Object> failDownload(HttpServletResponse response, @RequestBody @Valid FileGenReqDTO fileGenReqDTO){
	    HeaderSuccess header = HeaderSuccess.builder().build();
	    Success<Object> result = Success.builder().header(header).build();
	    
		try {
			commonService.createPackage(response, fileGenReqDTO);
		} catch (ValidException e) {
			log.error("CommonController failDownload ERROR : {}", e.getMessage());
			
			header.setMessage(e.getMessage());
			header.setResult(false);
			header.setCode("FAIL");
			result = Success.builder().header(header).build();
		}
        return result;
	}

}

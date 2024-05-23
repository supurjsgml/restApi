package com.app.common.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.common.core.annotations.ApiDocumentResponse;
import com.app.common.dto.req.FileGenReqDTO;
import com.app.common.exception.ValidException;
import com.app.common.service.CommonService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
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
	@GetMapping(value = "/genFile", produces = MediaType.APPLICATION_JSON_VALUE)
    public void failDownload(HttpServletResponse response, @ModelAttribute FileGenReqDTO fileGenReqDTO){
		try {
			commonService.fileDownload(response, fileGenReqDTO);
		} catch (ValidException e) {
			log.error("CommonController failDownload ERROR : {}", e.getMessage());
		}
	}

}

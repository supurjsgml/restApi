package com.app.member.controller;

import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.common.core.annotations.ApiDocumentResponse;
import com.app.common.dto.ApiBodyDTO;
import com.app.common.dto.ApiDocumentResponseDTO.Success.HeaderSuccess;
import com.app.common.enums.MessageEnum;
import com.app.common.exception.ValidException;
import com.app.common.util.ApiResUtil;
import com.app.common.util.MessagesUtils;
import com.app.member.dto.req.MemberReqDTO;
import com.app.member.service.MemberService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Tag(name = "회원", description = "회원")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/member")
public class MemberController {

	private final MemberService memberService;
	
	@ApiDocumentResponse
    @Operation(summary = "쥅코리아 이력서 갱신", description = "")
    @PostMapping(value = "/jobKorea/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiBodyDTO.Response<Map<String ,String>> getFileInfo(HttpServletRequest request, @RequestBody MemberReqDTO memberReqDTO){
	    ApiBodyDTO.Response<Map<String ,String>> result = null;
    	try {
    	    result = ApiResUtil.success(memberService.jobKoreaLogin(memberReqDTO), HeaderSuccess.builder().build(), MessageEnum.SUCCESS.getCode());
    	} catch (ValidException e) {
    		result = ApiResUtil.failed(e.getMessage());
    	} catch (Exception e) {
    		log.error("MemberController Exception ERROR : {}", e.getMessage());
    		result = ApiResUtil.failed(MessagesUtils.getMessage(MessageEnum.INTERNAL_SERVER_ERROR.getCode()));
		}
    	
    	return result;
    }

}

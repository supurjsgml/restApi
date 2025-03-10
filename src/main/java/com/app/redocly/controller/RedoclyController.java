package com.app.redocly.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.common.core.annotations.ApiDocumentResponse;
import com.app.redocly.dto.req.SampleDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Tag(name = "샘플1",description = "sample RedoclyController")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api")
public class RedoclyController {

	@ApiDocumentResponse
    @Operation(summary = "GET", description = "조회")
	@GetMapping(value = "/getUrl")
    public void test1(){
		
    }
	
	@ApiDocumentResponse
	@Operation(summary = "POST", description = "등록")
	@PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE)
	public void test2(@RequestBody SampleDTO sampleDTO){
		
	}
	
	@ApiDocumentResponse
	@Operation(summary = "PUT", description = "수정")
	@PutMapping(value = "/mod")
	public void test3(){
		
	}
	
	@ApiDocumentResponse
	@Operation(summary = "DELETE", description = "삭제")
	@DeleteMapping(value = "/del")
	public void test4(){
		
	}
}

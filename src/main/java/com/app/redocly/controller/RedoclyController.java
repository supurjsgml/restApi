package com.app.redocly.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.core.annotations.ApiDocumentResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Tag(name = "sample",description = "sample RedoclyController")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api")
public class RedoclyController {

	@ApiDocumentResponse
    @Operation(summary = "현만", description = "현만 조회")
	@GetMapping(value = "/getUrl")
    public void test1(){
		
    }
	
	@ApiDocumentResponse
	@Operation(summary = "샘출", description = "샘출 등록")
	@PostMapping(value = "/add")
	public void test2(){
		
	}
	
	@ApiDocumentResponse
	@Operation(summary = "병모", description = "병모 수정")
	@PutMapping(value = "/mod")
	public void test3(){
		
	}
	
	@ApiDocumentResponse
	@Operation(summary = "개수한무", description = "개수한무 삭제")
	@DeleteMapping(value = "/del")
	public void test4(){
		
	}
}

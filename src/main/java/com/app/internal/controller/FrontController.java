package com.app.internal.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.common.core.annotations.ApiDocumentResponse;
import com.app.common.dto.ApiBodyDTO;
import com.app.common.util.ApiResUtil;
import com.app.internal.dto.DashboardStatsDTO;
import com.app.internal.service.FrontService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Tag(name = "FRONT",description = "FrontController")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/front")
public class FrontController {

	private final FrontService frontService;

	@ApiDocumentResponse
	@Operation(summary = "대시보드 통계 정보 조회", description = "Dashboard Stats info")
	@GetMapping(value = "/dashboardStats", produces = MediaType.APPLICATION_JSON_VALUE)
	public ApiBodyDTO.Response<DashboardStatsDTO> getDashboardStats() {
		return ApiResUtil.success(frontService.getDashboardStats());
	}

	@ApiDocumentResponse
	@Operation(summary = "방문자 수 증가", description = "Increment visitor count")
	@PostMapping(value = "/hit")
	public ApiBodyDTO.Response<Void> incrementVisitor() {
		frontService.incrementVisitorCount();
		return ApiResUtil.success();
	}
}

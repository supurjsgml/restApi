package com.app.member.service.serviceimpl;

import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.app.common.exception.ValidException;
import com.app.member.dto.req.MemberReqDTO;
import com.app.member.service.MemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
	
	private final WebClient webClient;

	public Map<String, String> jobKoreaLogin(MemberReqDTO memberReqDTO) throws ValidException {
		Map<String, String> res = webClient.post()
				.uri("https://guney-batch.kro.kr/api/quartz/login")
				.bodyValue(memberReqDTO)
		        .retrieve()
		        .bodyToMono(new ParameterizedTypeReference<Map<String, String>>() {})
		        .doOnSuccess(response -> log.info("✅ 배치서버 통신 성공" + response))
		        .doOnError(error -> log.info("❌ 배치서버 통신 실패: " + error.getMessage()))
		        .block(); // 동기 실행
		
		if (ObjectUtils.isNotEmpty(res) && ObjectUtils.isNotEmpty(res.get("errCode"))) {
			throw new ValidException(res.get("errMsg"));
		}
		
	    return res;
	}

}

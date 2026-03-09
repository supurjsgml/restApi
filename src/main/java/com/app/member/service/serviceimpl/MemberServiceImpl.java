package com.app.member.service.serviceimpl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import com.app.common.constants.RestApiProperties;
import com.app.common.exception.ValidException;
import com.app.common.util.WebClientUtil;
import com.app.member.dto.req.MemberReqDTO;
import com.app.member.service.MemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
	
	private final WebClientUtil webClientUtil;
	
	private final RestApiProperties restApiProperties;

	@SuppressWarnings("unchecked")
	public Map<String, String> jobKoreaLogin(MemberReqDTO memberReqDTO) throws ValidException {
		Mono<HashMap> mono = webClientUtil.postAsync(restApiProperties.getBatch().getBaseUrl().concat("/api/quartz/login"), memberReqDTO, HashMap.class);
		
		Map<String, String> res = mono
		        .doOnSuccess(response -> log.info("배치서버 통신 성공 ID : {}, response : {}", memberReqDTO.getId(), response))
		        .doOnError(error -> log.error("배치서버 통신 실패 : {}", error.getMessage()))
		        .block();
		
		if (ObjectUtils.isEmpty(res) || ObjectUtils.isNotEmpty(res.get("errCode"))) {
			throw new ValidException(res.get("errMsg"));
		}
		
	    return res;
	}
	
	public void jobKoreaLogout(MemberReqDTO memberReqDTO) {
		webClientUtil.postAsync(restApiProperties.getBatch().getBaseUrl().concat("/api/quartz/pause?triggerName=").concat(memberReqDTO.getId()).concat("&groupName=userGroup"), Void.class)
					 .subscribe();
	}

}

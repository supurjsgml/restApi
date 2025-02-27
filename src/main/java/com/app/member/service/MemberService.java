package com.app.member.service;

import java.util.Map;

import com.app.common.exception.ValidException;
import com.app.member.dto.req.MemberReqDTO;

public interface MemberService {
    
    public Map<String ,String> jobKoreaLogin(MemberReqDTO memberReqDTO) throws ValidException;
    
}

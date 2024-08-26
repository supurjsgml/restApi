package com.app.common.service;

import java.util.Map;

import com.app.common.dto.req.FileGenReqDTO;
import com.app.common.exception.ValidException;

import jakarta.servlet.http.HttpServletResponse;

public interface CommonService {
    
    public void createPackage(HttpServletResponse response, FileGenReqDTO fileGenReqDTO) throws ValidException;

    public Map<String ,String> getFileInfo(HttpServletResponse response, FileGenReqDTO fileGenReqDTO);
    
}

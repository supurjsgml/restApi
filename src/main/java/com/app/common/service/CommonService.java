package com.app.common.service;

import com.app.common.dto.req.FileGenReqDTO;
import com.app.common.exception.ValidException;

import jakarta.servlet.http.HttpServletResponse;

public interface CommonService {
    public void fileDownload(HttpServletResponse response, FileGenReqDTO fileGenReqDTO) throws ValidException;
}

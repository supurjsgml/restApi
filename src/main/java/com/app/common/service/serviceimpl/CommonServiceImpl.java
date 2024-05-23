package com.app.common.service.serviceimpl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLEncoder;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import com.app.common.constant.CommonConstant;
import com.app.common.dto.req.FileGenReqDTO;
import com.app.common.exception.DataCustomException;
import com.app.common.exception.ValidException;
import com.app.common.service.CommonService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommonServiceImpl implements CommonService {

    private final CommonConstant commonConstant;
    
    /**
     * 파일 다운로드
     * 
     * @parma  : HttpServletResponse response, File file
     * @return : void
     * @throws IOException 
     * @user   : guney
     * @date   : 2024. 4. 14.
     * @since  : 1.0
     */
    public void fileDownload(HttpServletResponse response, FileGenReqDTO fileGenReqDTO) throws ValidException {
        log.info("fileGenReqDTO : {}", fileGenReqDTO);
        
        try {
            String filePathParam = StringUtils.isBlank(fileGenReqDTO.getFilePath()) ? commonConstant.FILE_PATH : fileGenReqDTO.getFilePath(); 
            File file = new File(filePathParam);
            
            if (!file.exists()) {
                //디렉토리 생성
                file.mkdirs();
            }
            
            file = new File(file.getPath().concat("/".concat(fileGenReqDTO.getFileNm())));
            
            //파일생성
            if (file.createNewFile()) {
                FileWriter fileWriter = new FileWriter(file);
//                fileWriter.write(fileGenReqDTO.getCamelStr());
                fileWriter.close();
                
                if (StringUtils.isBlank(fileGenReqDTO.getFilePath())) {
                    String fileName = file.getName();
                    String saveFileName = filePathParam.concat("/".concat(fileName));
                    
                    long fileLength = file.length();
                    
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < fileName.length(); i++) {
                        char c = fileName.charAt(i);
                        if (c > '~') {
                            sb.append(URLEncoder.encode("" + c, "UTF-8"));
                        } else {
                            sb.append(c);
                        }
                    }
                    
                    fileName = sb.toString();
                    
                    response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ";");
                    response.setHeader("Content-Transfer-Encoding", "binary");
                    response.setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);
                    response.setHeader("Content-Length", "" + fileLength);
                    response.setHeader("Pragma", "no-cache;");
                    response.setHeader("Expires", "-1;");
                    
                    FileInputStream fis = new FileInputStream(saveFileName);
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    
                    int readCount = 0;
                    byte[] buffer = new byte[1024];
                    
                    while((readCount = fis.read(buffer)) != -1){
                        out.write(buffer, 0, readCount);
                    }
                    out.writeTo(response.getOutputStream());
                    
                    fis.close();
                    out.close();
                    
                    //파일삭제
                    if (!file.delete()) {
                        log.error("delete File Info : {}", file);
                        throw new ValidException("파일 삭제에 실패하였습니다.");
                    }
                }
            }
        } catch(IOException ex){
            log.error("fileDownload ERROR : {}", ex.getMessage());
            throw new DataCustomException("file Save Error");
        }
    }
}

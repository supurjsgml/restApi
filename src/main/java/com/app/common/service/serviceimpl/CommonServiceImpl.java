package com.app.common.service.serviceimpl;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.stereotype.Service;

import com.app.common.constant.CommonConstant;
import com.app.common.dto.req.FileGenReqDTO;
import com.app.common.enums.TemplatesEnum;
import com.app.common.exception.DataCustomException;
import com.app.common.exception.ValidException;
import com.app.common.service.CommonService;
import com.app.common.util.CommonUtil;
import com.app.common.util.FileUtil;
import com.app.common.util.RexUtil;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommonServiceImpl implements CommonService {

    private final CommonConstant commonConstant;
    
    private final VelocityEngine velocityEngine;
    
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
    public void createPackage(HttpServletResponse response, FileGenReqDTO fileGenReqDTO) throws ValidException {
        try {
            String fileNm = CommonUtil.capitalize(fileGenReqDTO.getFileNm());
            String dtoNm = CommonUtil.capitalize(fileNm).concat(TemplatesEnum.DTO.getFileNm());
            String filePathParam = StringUtils.isBlank(fileGenReqDTO.getFilePath()) ? commonConstant.FILE_PATH : fileGenReqDTO.getFilePath();
//            String filePathParam = commonConstant.FILE_PATH;
            
            for (TemplatesEnum tpl : TemplatesEnum.values()) {
                
                File file = new File(filePathParam.concat("/")
                                                  .concat(TemplatesEnum.IMPLEMENT.getFolderNm().equals(tpl.getFolderNm())
                                                		? TemplatesEnum.SERVICE.getFolderNm().concat("/").concat(tpl.getFolderNm())
                                                        : tpl.getFolderNm()));
                
                if (!file.exists()) {
                    //디렉토리 생성
                    file.mkdirs();
                }
                
                file = new File(file.getPath().concat("/")
                                              .concat(fileNm)
                                              .concat(tpl.getFileNm())
                                              .concat(".java"));
                
                //파일생성
                if (file.createNewFile()) {
//                D:/sts-4.15.1.RELEASE/workspace/restApi/src/main/java/com/app/go
                    
                    VelocityContext context = new VelocityContext();
                    
                    context.put("packPath", filePathParam.contains("com")
                        ? filePathParam.substring(filePathParam.indexOf("com"), filePathParam.length()).replaceAll(RexUtil.SLASH_BACKSLASH_PATTERN, ".")
                        : "com.example");
                    
                    context.put("fileNm", fileNm);
                    context.put("fileVarName", CommonUtil.decapitalize(fileNm));
                    context.put("methodNm", fileGenReqDTO.getMethodNm());
                    context.put("dtoNm", dtoNm);
                    context.put("dtoVarName", CommonUtil.decapitalize(dtoNm));
                    
                    // 템플릿 파일 로딩
                    Template template = velocityEngine.getTemplate(tpl.getVmPath());
                    StringWriter writer = new StringWriter();
                    template.merge(context, writer);
                    
                    FileUtil.writeFile(file, writer.toString());
                } else {
                	log.info("filePath : {}", file.getPath());
                    throw new ValidException("파일 생성에 실패하였습니다.");
                }
            }
            
        } catch(IOException ex){
            log.error("fileDownload ERROR : {}", ex.getMessage());
            throw new DataCustomException("file Save Error");
        }
    }
    
    public Map<String ,String> getFileInfo(HttpServletResponse response, FileGenReqDTO fileGenReqDTO) {
        Map<String ,String> result = new HashMap<>();
        
        String fileNm = CommonUtil.capitalize(fileGenReqDTO.getFileNm());
        String dtoNm = CommonUtil.capitalize(fileNm).concat(TemplatesEnum.DTO.getFileNm());
        String filePathParam = StringUtils.isBlank(fileGenReqDTO.getFilePath()) ? commonConstant.FILE_PATH : fileGenReqDTO.getFilePath();
        
        for (TemplatesEnum tpl : TemplatesEnum.values()) {
            VelocityContext context = new VelocityContext();
            
            context.put("packPath", filePathParam.contains("com")
                ? filePathParam.substring(filePathParam.indexOf("com"), filePathParam.length()).replaceAll(RexUtil.SLASH_BACKSLASH_PATTERN, ".")
                    : "com.example");
            
            context.put("fileNm", fileNm);
            context.put("fileVarName", CommonUtil.decapitalize(fileNm));
            context.put("methodNm", fileGenReqDTO.getMethodNm());
            context.put("dtoNm", dtoNm);
            context.put("dtoVarName", CommonUtil.decapitalize(dtoNm));
            
            // 템플릿 파일 로딩
            Template template = velocityEngine.getTemplate(tpl.getVmPath());
            StringWriter writer = new StringWriter();
            template.merge(context, writer);
            
            result.put(tpl.getFolderNm(), writer.toString());
        }
            
        return result;
    }
}

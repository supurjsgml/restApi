package com.app.common.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.common.constant.CommonConstant;
import com.app.common.core.annotations.ApiDocumentResponse;
import com.app.common.dto.ApiBodyDTO;
import com.app.common.dto.req.FileGenReqDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Tag(name = "공통", description = "공통 유틸 이랄까")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/com")
public class CommonController {

	@Autowired
	private CommonConstant commonConstant;
	
	@ApiDocumentResponse
    @Operation(summary = "파일생성", description = "File Generator")
	@PostMapping(value = "/genFile", produces = MediaType.APPLICATION_JSON_VALUE)
    public void failDownload(HttpServletResponse response, @RequestBody ApiBodyDTO.Request<FileGenReqDTO> fileGenReqDTO){
		log.info("FILE_PATH : {}, fileGenReqDTO : {}", commonConstant.FILE_PATH, fileGenReqDTO);
		
		File file = new File(commonConstant.FILE_PATH);
		
		if (!file.exists()) {
			file.mkdirs();
		}
		
		try {
			file = new File(file.getPath().concat("/filename.java"));
			
			if (file.createNewFile()) {
				FileWriter fileWriter = new FileWriter(file);
				fileWriter.write(fileGenReqDTO.getData().getCamelStr());
				fileWriter.close();
				
				fileDownload(response, file);
			} else {
				log.info("File already exists.");
			}
		} catch (IOException e) {
			log.error("An error occurred.");
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * 
	 * @parma  : File file
	 * @return : void
	 * @throws UnsupportedEncodingException 
	 * @user   : guney
	 * @date   : 2024. 4. 14.
	 * @since  : 1.0
	 */
	public void fileDownload(HttpServletResponse response, File file) throws UnsupportedEncodingException {
		if (file.isFile()) {
			String fileName = "filename.java";
			String saveFileName = commonConstant.FILE_PATH.concat("/".concat(fileName));
			
			long fileLength = file.length();
			
			StringBuffer sb = new StringBuffer();
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
			
			try {
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
				
				if (!file.delete()) {
					log.error("delete File Info : {}", file);
				}
				
			} catch(Exception ex){
				throw new RuntimeException("file Save Error");
			}
		}
	}

}

package com.app.common.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.common.constant.CommonConstant;
import com.app.common.core.annotations.ApiDocumentResponse;

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
	@GetMapping(value = "/genFile")
    public void failDownload(HttpServletResponse response){
		log.info("FILE_PATH : {}", commonConstant.FILE_PATH);
		
		File file = new File(commonConstant.FILE_PATH);
		
		if (!file.exists()) {
			file.mkdirs();
		}
		
		try {
			file = new File(file.getPath().concat("/filename.java"));
			
			if (file.createNewFile()) {
				FileWriter fileWriter = new FileWriter(file);
				fileWriter.write("Files in Java might be tricky, but it is fun enough!");
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
	 * @user   : guney
	 * @date   : 2024. 4. 14.
	 * @since  : 1.0
	 */
	public void fileDownload(HttpServletResponse response, File file) {
		if (file.isFile()) {
			String fileName = "/filename.java";
			String saveFileName = commonConstant.FILE_PATH.concat(fileName);
			String contentType = "text";
			
			long fileLength = file.length();
			
			response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\";");
			response.setHeader("Content-Transfer-Encoding", "binary");
			response.setHeader("Content-Type", contentType);
			response.setHeader("Content-Length", "" + fileLength);
			response.setHeader("Pragma", "no-cache;");
			response.setHeader("Expires", "-1;");
			
			try {
				FileInputStream fis = new FileInputStream(saveFileName);
				OutputStream out = response.getOutputStream();
				
				int readCount = 0;
				byte[] buffer = new byte[1024];
				
				while((readCount = fis.read(buffer)) != -1){
					out.write(buffer,0,readCount);
				}
				
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

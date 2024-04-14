package com.app.common.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.common.constant.CommonConstant;
import com.app.common.core.annotations.ApiDocumentResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
    public void failDownload(){
		log.info("tttttttttttt : " + commonConstant.FILE_PATH);
		
		File file = new File(commonConstant.FILE_PATH);
		
		if (!file.exists()) {
			file.mkdirs();
		}
		
		try {
			file = new File(file.getPath().concat("/filename.txt"));
			
			if (file.createNewFile()) {
				FileWriter fileWriter = new FileWriter(file);
				fileWriter.write("Files in Java might be tricky, but it is fun enough!");
				fileWriter.close();
				
			} else {
				System.out.println("File already exists.");
			}
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}

}

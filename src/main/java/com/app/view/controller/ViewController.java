package com.app.view.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ViewController {

	@GetMapping("/")
	public String camel(){
		return "html/ConvertCamel";
	}
	
	@GetMapping("/redoc")
    public String redoc(){
		return "html/index";
    }
	
	@GetMapping("/jsp")
	public String jsp(){
		return "ConvertCamel";
	}
	
}

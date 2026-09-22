package com.studymate.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.studymate.service.EmailService;

@Controller
public class MainController {

	@GetMapping("/")
	public String main() {
		return "redirect:/study/main";
	}
}

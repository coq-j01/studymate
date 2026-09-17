package com.studymate.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.studymate.dto.StudyDTO;
import com.studymate.security.CustomUserDetails;
import com.studymate.service.StudyService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/study")
public class StudyController {
	private final StudyService studyService;
	
	//create페이지로 이동
	@GetMapping("/create")
	public String create() {
		return "study/create";
	}
	//스터디 생성
	@PostMapping("/create")
	public String postCreate(StudyDTO studyDTO,
			@AuthenticationPrincipal CustomUserDetails userDetails) {
		int studyId = studyService.createStudy(studyDTO,userDetails.getMemberId());
		/* return "redirect:/study/" + studyId; */
		return "redirect:/study/main";
	}
	
}

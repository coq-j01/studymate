package com.studymate.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.studymate.domain.Study;
import com.studymate.security.CustomUserDetails;
import com.studymate.service.StudyMemberService;
import com.studymate.service.StudyService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/study/{studyId}")
public class StudyBoardController {
	private final StudyService studyService;
	private final StudyMemberService studyMemberService;
	
	@ModelAttribute
	public void addStudyId(
	        @PathVariable int studyId,
	        Model model) {

	    model.addAttribute("study", studyService.getStudy(studyId));
	}

	@GetMapping("/home")
	public String home(@PathVariable int studyId, @AuthenticationPrincipal CustomUserDetails userDetails, Model model) {

		if (!studyMemberService.canAccessStudy(
	            studyId,
	            userDetails.getMemberId())) {

	        return "redirect:/study/detail/" + studyId;
	    }
		
		return "study/board/home";
	}

	@GetMapping("/notice")
	public String notice(
	        @PathVariable int studyId,
	        @AuthenticationPrincipal CustomUserDetails userDetails,
	        Model model) {

	    if (!studyMemberService.canAccessStudy(
	            studyId,
	            userDetails.getMemberId())) {

	        return "redirect:/study/detail/" + studyId;
	    }

	    // model.addAttribute("noticeList", ...);

	    return "study/board/notice";
	}

	@GetMapping("/attendance")
	public String attendance(
	        @PathVariable int studyId,
	        @AuthenticationPrincipal CustomUserDetails userDetails,
	        Model model) {

	    if (!studyMemberService.canAccessStudy(
	            studyId,
	            userDetails.getMemberId())) {

	        return "redirect:/study/detail/" + studyId;
	    }

	    // model.addAttribute("noticeList", ...);

	    return "study/board/attendance";
	}
	@GetMapping("/members")
	public String getMembers(@PathVariable int studyId, @AuthenticationPrincipal CustomUserDetails userDetails, Model model) {

		model.addAttribute("memberList", studyMemberService.getStudyMemberList(studyId));
		
		model.addAttribute("maxMember", studyService.getMaxMember(studyId));
		
		return "study/board/members";
	}
}

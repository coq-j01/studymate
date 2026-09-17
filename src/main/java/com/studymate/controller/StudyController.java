package com.studymate.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.studymate.domain.Study;
import com.studymate.dto.StudyDTO;
import com.studymate.security.CustomUserDetails;
import com.studymate.service.StudyService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/study")
public class StudyController {
	private final StudyService studyService;

	// create페이지로 이동
	@GetMapping("/create")
	public String create() {
		return "study/create";
	}

	// 스터디 생성
	@PostMapping("/create")
	public String postCreate(StudyDTO studyDTO, @AuthenticationPrincipal CustomUserDetails userDetails) {
		int studyId = studyService.createStudy(studyDTO, userDetails.getMemberId());
		/* return "redirect:/study/" + studyId; */
		return "redirect:/study/main";
	}

	// 메인페이지(스터디 목록 가져오기)
	@GetMapping("/main")
	public String getmain(@RequestParam(required = false) String keyword,
			@RequestParam(required = false) Integer categoryId, Model model) {
		List<Study> studyList = studyService.getStudyList(keyword, categoryId);

		model.addAttribute("studyList", studyList);
		model.addAttribute("keyword", keyword);
		model.addAttribute("categoryId", categoryId);

		return "study/main";
	}

	// 메인페이지(스터디 목록 가져오기)
	@GetMapping("/my-study")
	public String getMyStudy(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
		List<Study> myStudyList = studyService.getMyStudyList(userDetails.getMemberId());

		model.addAttribute("myStudyList", myStudyList);

		if (userDetails != null) {
			model.addAttribute("loginMemberId", userDetails.getMemberId());
		}

		return "study/my-study";
	}

	//스터디 상세보기
	@GetMapping("/detail/{studyId}")
	public String getDetail(@PathVariable int studyId, Model model
			, @AuthenticationPrincipal CustomUserDetails userDetails) {
		model.addAttribute("study", studyService.getStudy(studyId));
		model.addAttribute("status",studyService.findStudyMemberStatus(studyId, userDetails.getMemberId()));
		return "study/detail";
	}
	//스터디 수정페이지
	@GetMapping("/edit/{studyId}")
	public String getEdit(@PathVariable int studyId, Model model
			, @AuthenticationPrincipal CustomUserDetails userDetails) {
		Study study = studyService.getStudy(studyId);
		// 스터디장인지 확인
	    if (study.getLeaderId() != userDetails.getMemberId()) {
	        return "redirect:/study/detail/" + studyId;
	    }

	    model.addAttribute("study", study);
		return "study/edit";
	}
	//수정 완료
	@PostMapping("/edit/{studyId}")
	public String postEdit(
	        @PathVariable int studyId,
	        StudyDTO studyDTO,
	        @AuthenticationPrincipal CustomUserDetails userDetails) {

	    studyDTO.setStudyId(studyId);

	    studyService.updateStudy(
	            studyDTO,
	            userDetails.getMemberId()
	    );

	    return "redirect:/study/detail/" + studyId;
	}
	//스터디 신청
	@PostMapping("/{studyId}/apply")
	public String applyStudy(@PathVariable int studyId,
	        @RequestParam String message,
	        @AuthenticationPrincipal CustomUserDetails userDetails) {
		studyService.applyStudy(studyId, userDetails.getMemberId(), message);
		
		return "redirect:/study/detail/" + studyId;
	}
	
}

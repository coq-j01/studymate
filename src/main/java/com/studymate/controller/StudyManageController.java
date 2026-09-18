package com.studymate.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.studymate.mapper.StudyJoinRequestMapper;
import com.studymate.mapper.StudyMapper;
import com.studymate.security.CustomUserDetails;
import com.studymate.service.StudyMemberService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/study/{studyId}/manage")
public class StudyManageController {
	private final StudyMapper studyMapper;
	private final StudyMemberService memberService;
	
	private boolean isLeader(int studyId,int loginMemberId) {
		return studyMapper.isStudyLeader(studyId,loginMemberId);
	}
	//신청서 페이지
	@GetMapping("/requests")
    public String requests(@PathVariable int studyId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            Model model) {
		if(!isLeader(studyId,userDetails.getMemberId())) {
			return "redirect:/study/" + studyId + "/home";
		}
		model.addAttribute("requestList",memberService.getJoinRequestList(studyId));
		return "study/manage/requests";
    }
	//신청서 승인
	@PostMapping("/requests/{memberId}/approve")
	public ResponseEntity<String> approveJoinRequest(
	        @PathVariable int studyId,
	        @PathVariable int memberId,
	        @AuthenticationPrincipal CustomUserDetails userDetails){
		try {

			memberService.approveJoinRequest(
	                studyId,
	                memberId,
	                userDetails.getMemberId()
	        );

	        return ResponseEntity.ok("가입 신청을 수락했습니다.");

	    } catch (IllegalStateException e) {

	        return ResponseEntity
	                .badRequest()
	                .body(e.getMessage());
	    }
	}
	
	//신청서 거절
		@PostMapping("/requests/{memberId}/reject")
		public ResponseEntity<String> rejectJoinRequest(
		        @PathVariable int studyId,
		        @PathVariable int memberId,
		        @AuthenticationPrincipal CustomUserDetails userDetails){
			try {

				memberService.rejectJoinRequest(
		                studyId,
		                memberId,
		                userDetails.getMemberId()
		        );

		        return ResponseEntity.ok("가입 신청을 거절했습니다.");

		    } catch (IllegalStateException e) {

		        return ResponseEntity
		                .badRequest()
		                .body(e.getMessage());
		    }
		}

	/*
	 * @GetMapping("/members") public String members(...) { ... }
	 * 
	 * @GetMapping("/settings") public String settings(...) { ... }
	 */
}

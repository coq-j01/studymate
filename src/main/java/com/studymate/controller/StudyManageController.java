package com.studymate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.studymate.security.CustomUserDetails;
import com.studymate.service.StudyMemberService;
import com.studymate.service.StudyService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/study/{studyId}/manage")
public class StudyManageController {
	private final StudyMemberService studyMemberService;
	private final StudyService studyService;

	@ModelAttribute
	public void addStudyId(@PathVariable int studyId, Model model) {

		model.addAttribute("studyId", studyId);
	}

	private boolean isLeader(int studyId, int loginMemberId) {
		return studyService.isLeader(studyId, loginMemberId);
	}

	// 신청서 페이지
	@GetMapping("/requests")
	public String requests(@PathVariable int studyId, @AuthenticationPrincipal CustomUserDetails userDetails,
			Model model) {
		if (!isLeader(studyId, userDetails.getMemberId())) {
			return "redirect:/study/" + studyId + "/home";
		}
		model.addAttribute("joinRequestList", studyMemberService.getJoinRequestList(studyId));
		return "study/manage/requests";
	}

	// 신청서 승인
	@PostMapping("/requests/{memberId}/approve")
	public ResponseEntity<String> approveJoinRequest(@PathVariable int studyId, @PathVariable int memberId,
			@AuthenticationPrincipal CustomUserDetails userDetails) {
		try {

			studyMemberService.approveJoinRequest(studyId, memberId, userDetails.getMemberId());

			return ResponseEntity.ok("가입 신청을 수락했습니다.");

		} catch (IllegalStateException e) {

			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	// 신청서 거절
	@PostMapping("/requests/{memberId}/reject")
	public ResponseEntity<String> rejectJoinRequest(@PathVariable int studyId, @PathVariable int memberId,
			@AuthenticationPrincipal CustomUserDetails userDetails) {
		try {

			studyMemberService.rejectJoinRequest(studyId, memberId, userDetails.getMemberId());

			return ResponseEntity.ok("가입 신청을 거절했습니다.");

		} catch (IllegalStateException e) {

			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	// 강퇴하기
	@PostMapping("/members/{memberId}/kick")
	public ResponseEntity<String> kickMember(@PathVariable int studyId, @PathVariable int memberId,
			@AuthenticationPrincipal CustomUserDetails userDetails) {
		if (!isLeader(studyId, userDetails.getMemberId())) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("스터디장만 강퇴할 수 있습니다.");
		}
		studyMemberService.inactiveMember(studyId, memberId);

		return ResponseEntity.ok("스터디원을 강퇴했습니다.");

	}

	@GetMapping("/members")
	public String getMembers(@PathVariable int studyId, @AuthenticationPrincipal CustomUserDetails userDetails,
			Model model) {
		model.addAttribute("memberList", studyMemberService.getStudyMemberList(studyId));
		if (!isLeader(studyId, userDetails.getMemberId())) {
			return "redirect:/study/" + studyId + "/home";
		}
		model.addAttribute("memberList", studyMemberService.getStudyMemberList(studyId));

		return "study/manage/members";
	}

	/*
	 * @GetMapping("/settings") public String settings(...) { ... }
	 */
}

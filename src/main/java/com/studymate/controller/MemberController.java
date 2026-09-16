package com.studymate.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.studymate.dto.JoinDTO;
import com.studymate.service.MemberService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MemberController {
	private final MemberService memberService;
	//login페이지 이동
	@GetMapping("/login")
	public String login() {
		return "member/login";
	}
	
	//join페이지로 이동
	@GetMapping("/join")
	public String join() {
		return "member/join";
	}
	
	//POST Join
	@PostMapping("/join")
	public String postJoin(JoinDTO joinDTO){
		if(memberService.join(joinDTO)) {
			return "redirect:/login";
		}
		//컨트롤러 호출이 아닌 바로 템플릿 렌더링
		return "member/join";
	}
	
	//이메일 중복 확인
	@GetMapping("/check/email")
	@ResponseBody
	public boolean checkEmail(String email) {
		return memberService.isEmailDuplicate(email);
	}
	
	//닉네임 중복 확인
	@GetMapping("/check/nickname")
	@ResponseBody
	public boolean checkNickname(String nickname) {
		return memberService.isNicknameDuplicate(nickname);
	}
	
}

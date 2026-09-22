package com.studymate.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.studymate.domain.Member;
import com.studymate.dto.JoinDTO;
import com.studymate.dto.SocialJoinDTO;
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
	
	@GetMapping("/social/join")
	public String socialJoin(HttpSession session, Model model) {

        String email = (String) session.getAttribute("socialEmail");
        String name = (String) session.getAttribute("socialName");

        // Google 로그인 정보 없이 직접 접근한 경우
        if (email == null || name == null) {
            return "redirect:/login";
        }

        model.addAttribute("email", email);
        model.addAttribute("name", name);

        return "member/socialJoin";
    }
	
	@PostMapping("/social/join")
	public String postSocialJoin(@Valid SocialJoinDTO socialJoinDTO,BindingResult bindingResult,
	        HttpSession session) {

		if (bindingResult.hasErrors()) {
	        return "member/socialJoin";
	    }
		
		String email = (String) session.getAttribute("socialEmail");
	    String name = (String) session.getAttribute("socialName");
	    String provider = (String) session.getAttribute("socialProvider");
	    String providerId = (String) session.getAttribute("socialProviderId");

	    // 세션 정보가 없으면 잘못된 접근
	    if (email == null || name == null ||
	        provider == null || providerId == null) {

	        return "redirect:/login";
	    }

	    Member member = new Member();

	    member.setEmail(email);
	    member.setName(name);
	    member.setNickname(socialJoinDTO.getNickname());
	    member.setGender(socialJoinDTO.getGender());
	    member.setProvider(provider);
	    member.setProviderId(providerId);

	    memberService.joinSocialMember(member);

	    return "redirect:/login";
    }
	
}

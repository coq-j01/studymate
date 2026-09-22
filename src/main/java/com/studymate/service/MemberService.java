package com.studymate.service;

import com.studymate.domain.Member;
import com.studymate.dto.JoinDTO;

public interface MemberService {
	boolean join(JoinDTO joindto);
	boolean isEmailDuplicate(String email);
	boolean isNicknameDuplicate(String nickname);
	String getEmail(int memberId);
	int joinSocialMember(Member member);
	Member findSocialLogin(String provider, String providerId); 
}

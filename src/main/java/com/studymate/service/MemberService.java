package com.studymate.service;

import com.studymate.dto.JoinDTO;

public interface MemberService {
	void join(JoinDTO joindto);
	boolean isEmailDuplicate(String email);
	boolean isNicknameDuplicate(String nickname);
}

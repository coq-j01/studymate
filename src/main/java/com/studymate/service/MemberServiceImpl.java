package com.studymate.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.studymate.dto.JoinDTO;
import com.studymate.mapper.MemberMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
	private final MemberMapper memberMapper;
	private final PasswordEncoder passwordEncoder;

	@Override
	public boolean isEmailDuplicate(String email) {
		if(memberMapper.findByEmail(email) == null) {
			return false;
		}
		return true;
	}

	@Override
	public boolean isNicknameDuplicate(String nickname) {
		if(memberMapper.findByNickname(nickname) == null) {
			return false;
		}
		return true;
	}

	@Override
	public void join(JoinDTO joindto) {
		if(!isNicknameDuplicate(joindto.getNickname()) && !isEmailDuplicate(joindto.getEmail())
				&& joindto.getPassword().equals(joindto.getPasswordConfirm())) {
			joindto.setPassword(passwordEncoder.encode(joindto.getPassword()));
			memberMapper.insertMember(joindto);
		}
	}
}

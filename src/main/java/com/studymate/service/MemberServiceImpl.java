package com.studymate.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.studymate.domain.Member;
import com.studymate.dto.JoinDTO;
import com.studymate.dto.SocialJoinDTO;
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
	public boolean join(JoinDTO joindto) {
		if(!isNicknameDuplicate(joindto.getNickname()) && !isEmailDuplicate(joindto.getEmail())
				&& joindto.getPassword().equals(joindto.getPasswordConfirm())) {
			joindto.setPassword(passwordEncoder.encode(joindto.getPassword()));
			int result = memberMapper.insertMember(joindto);
			return result == 1;
		}
		return false;
	}

	@Override
	public String getEmail(int memberId) {
		return memberMapper.findEmailById(memberId);
	}
	
	public int joinSocialMember(Member member) {
	    return memberMapper.insertSocialMember(member);
	}

	@Override
	public Member findSocialLogin(String provider, String providerId) {
		return memberMapper.findByProviderAndProviderId(provider, providerId);
	}
}

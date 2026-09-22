package com.studymate.security;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.studymate.domain.Member;
import com.studymate.service.MemberService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
	// Google 정보를 받아서 우리 DB 회원과 연결하는 곳
	private final MemberService memberService;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

		// 1. Google에서 사용자 정보 받아오기
		OAuth2User oAuth2User = super.loadUser(userRequest);
		// 2. 어떤 소셜 로그인인지 확인
		String provider = userRequest.getClientRegistration().getRegistrationId().toUpperCase();
		// 3. Google 정보
		String providerId = oAuth2User.getAttribute("sub");
		String email = oAuth2User.getAttribute("email");
		
		System.out.println("조회 provider = [" + provider + "]");
		System.out.println("조회 providerId = [" + providerId + "]");

		// 4. 우리 DB에 가입된 회원인지 조회
		Member member = memberService.findSocialLogin(provider, providerId);

		// 5.기존에 가입한 Google 회원
		if (member != null) {
			return new CustomOAuth2User(member, oAuth2User.getAttributes());
		}
		
		// 6. 같은 이메일로 가입한 기존 회원 확인
        boolean isEmailDuplicate = memberService.isEmailDuplicate(email);
        
        if (isEmailDuplicate) {
            throw new OAuth2AuthenticationException(
                    new OAuth2Error("duplicate_email"),
                    "이미 일반 회원가입된 이메일입니다."
            );
        }

		// 7. 아직 StudyMate 회원가입 안 한 Google 사용자
		// 아직 StudyMate 회원이 아니므로 기본 OAuth2User 반환
		// SuccessHandler에서 /social/join으로 이동시킨다.
		return oAuth2User;
	}
}

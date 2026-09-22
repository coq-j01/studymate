package com.studymate.security;

import java.util.Map;

import org.springframework.security.oauth2.core.user.OAuth2User;

import com.studymate.domain.Member;

public class CustomOAuth2User extends CustomUserDetails implements OAuth2User {
	//Spring Security가 로그인 사용자로 들고 있을 객체
	
	//google이 준 원본 정보
	private final Map<String, Object> attributes;

    public CustomOAuth2User(
            Member member,
            Map<String, Object> attributes) {

        super(member);
        this.attributes = attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return super.getUsername();
    }
}

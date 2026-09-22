package com.studymate.security;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	//로그인이 성공한 뒤 어디로 보낼지 결정하는 곳
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException {

		Object principal = authentication.getPrincipal();

		// 이미 StudyMate에 가입한 Google 회원
		if (principal instanceof CustomOAuth2User) {

			response.sendRedirect("/");
			return;
		}

		// 처음 Google 로그인한 사용자
		OAuth2User oAuth2User = (OAuth2User) principal;

		String email = oAuth2User.getAttribute("email");
		String name = oAuth2User.getAttribute("name");
		String providerId = oAuth2User.getAttribute("sub");
		
		OAuth2AuthenticationToken oauthToken =
		        (OAuth2AuthenticationToken) authentication;

		String provider = oauthToken
		        .getAuthorizedClientRegistrationId()
		        .toUpperCase();

		request.getSession().setAttribute("socialEmail", email);
		request.getSession().setAttribute("socialName", name);
		request.getSession().setAttribute("socialProvider", provider);
		request.getSession().setAttribute("socialProviderId", providerId);

		response.sendRedirect("/social/join");
	}
}
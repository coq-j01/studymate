package com.studymate.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import com.studymate.security.CustomOAuth2UserService;
import com.studymate.security.OAuth2LoginSuccessHandler;

@Configuration
public class SecurityConfig {
	private final CustomOAuth2UserService customOAuth2UserService;
	private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
	
	public SecurityConfig(CustomOAuth2UserService customOAuth2UserService,
	        OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler) {
	    this.customOAuth2UserService = customOAuth2UserService;
	    this.oAuth2LoginSuccessHandler = oAuth2LoginSuccessHandler;
	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http.authorizeRequests().antMatchers("/login", "/join", "/css/**","/js/**", "/assets/**",
				"/check/**", "/oauth2/**", "/login/oauth2/**")
			.permitAll()
			.anyRequest().authenticated()
			.and()
			.formLogin().loginPage("/login").loginProcessingUrl("/login")
						.defaultSuccessUrl("/", true).failureUrl("/login?error")
						.permitAll()
			.and().oauth2Login().loginPage("/login").userInfoEndpoint()
	        		.userService(customOAuth2UserService)
	        		.and()
	        		.successHandler(oAuth2LoginSuccessHandler)
	        		.failureUrl("/login?socialError")
			.and()
			.logout()
	        	.logoutUrl("/logout")
	        	.logoutSuccessUrl("/login")
	        	.invalidateHttpSession(true)
	        	.deleteCookies("JSESSIONID")
	        .and()
	        .rememberMe()
	            .key("studymate-remember-key")
	            .tokenValiditySeconds(60 * 60 * 24 * 7);

		return http.build();
	}
}

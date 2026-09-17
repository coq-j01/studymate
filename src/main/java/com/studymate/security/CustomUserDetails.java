package com.studymate.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.studymate.domain.Member;

import lombok.Getter;

@Getter
public class CustomUserDetails implements UserDetails {
	
	private int memberId;
    private String email;
    private String password;
    private String nickname;
    
    public CustomUserDetails(Member member) {
        this.memberId = member.getMemberId();
        this.email = member.getEmail();
        this.password = member.getPassword();
        this.nickname = member.getNickname();
    }

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(
	            new SimpleGrantedAuthority("ROLE_USER")
	        );
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}

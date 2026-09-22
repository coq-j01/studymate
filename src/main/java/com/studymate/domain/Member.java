package com.studymate.domain;

import lombok.Data;

@Data
public class Member {
	private int memberId;
	private String email;
	private String password;
	private String name;
	private String nickname;
	private String gender;
	private String provider;
	private String providerId;
}
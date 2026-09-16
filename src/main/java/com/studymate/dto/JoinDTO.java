package com.studymate.dto;

import lombok.Data;

@Data
public class JoinDTO {
	private String email;
	private String password;
	private String passwordConfirm;
	private String name;
	private String nickname;
	private String gender;
}
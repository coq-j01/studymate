package com.studymate.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class JoinDTO {
	 	@NotBlank
	    @Email
	    private String email;

	    @NotBlank
	    @Size(min = 8)
	    private String password;

	    @NotBlank
	    private String passwordConfirm;

	    @NotBlank
	    private String name;

	    @NotBlank
	    private String nickname;

	    @NotBlank
	    @Pattern(regexp = "F|M")
	    private String gender;
}
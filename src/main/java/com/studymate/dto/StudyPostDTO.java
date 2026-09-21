package com.studymate.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class StudyPostDTO {
	@NotBlank
	@Size(max=20)
	private String title;
	@NotBlank
	private String content;
}

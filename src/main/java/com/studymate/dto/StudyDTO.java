package com.studymate.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class StudyDTO {
	private int studyId;
	
	@NotBlank
	@Size(max = 100)
	private String title;
	
	@NotBlank
	@Size(max = 500)
	private String description;
	
	@NotNull
	@Min(value = 1)
    @Max(value = 6)
	private int categoryId;
	
	@NotNull
	@Min(value = 2)
    @Max(value = 10)
	private int maxMember;
	
	private String thumbnail;
	private String studyStatus;
}

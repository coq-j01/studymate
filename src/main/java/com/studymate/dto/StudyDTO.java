package com.studymate.dto;

import lombok.Data;

@Data
public class StudyDTO {
	private int studyId;
	private String title;
	private String description;
	private int categoryId;
	private int maxMember;
	private String thumbnail;
}

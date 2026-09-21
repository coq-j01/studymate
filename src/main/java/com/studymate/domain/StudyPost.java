package com.studymate.domain;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class StudyPost {
	private int postId;
	private int studyId;
	private int memberId;
	private String postType;
	private String title;
	private String content;
	private LocalDateTime createdAt;
	private LocalDateTime updateAt;
	
	private String nickname;
	private int likeCount;
}

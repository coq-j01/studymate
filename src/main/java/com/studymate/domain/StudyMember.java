package com.studymate.domain;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class StudyMember {
	private int studyId;
	private int memberId;
	private LocalDateTime joinedAt;
	private String memberStatus;
	
	private String nickname;
	private String gender;
	
	private boolean leader;
	
	// 최근 게시글 작성일
    private LocalDateTime lastPostAt;
}

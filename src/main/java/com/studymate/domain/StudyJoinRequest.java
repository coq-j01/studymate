package com.studymate.domain;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class StudyJoinRequest {
	private int requestId;
	private int studyId;
	private int memberId;
	private String message;
	private LocalDateTime appliedAt;
	
	private String nickname;
	private String gender;
}

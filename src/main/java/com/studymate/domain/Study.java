package com.studymate.domain;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Study {
	private int studyId;
	private int leaderId;
	private String title;
	private String description;
	private int categoryId;
	private String categoryName;
	private int maxMember;
	private LocalDateTime createdAt;
	private LocalDateTime endedAt;
	private String studyStatus;
	private String thumbnail;
	
	private String leaderNickname;   // 스터디장 닉네임
	private int currentMemberCount;  // 현재 ACTIVE 스터디원 수
	
	private String thumbnailUrl; //S3에서 가져오는 화면 출력용 url
}

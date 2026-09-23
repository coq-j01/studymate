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
	
    private String leaderNickname;
    private int currentMemberCount;

    private String thumbnailUrl;
}

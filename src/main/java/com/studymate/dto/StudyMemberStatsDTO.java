package com.studymate.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class StudyMemberStatsDTO {
	private int memberId;

    private String nickname;
    private String gender;

    private LocalDateTime joinedAt;

    private boolean leader;

    // 최근 활동일
    private LocalDateTime lastPostAt;

    // 인증 참여율
    private int certificationRate;
}

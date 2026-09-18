package com.studymate.service;

import java.util.List;

import com.studymate.domain.StudyJoinRequest;

public interface StudyMemberService {
	//가입 승인
	void approveJoinRequest(int studyId, int memberId, int leaderId);
	
	//가입 거절
	void rejectJoinRequest(int studyId, int memberId, int leaderId);
	
	List<StudyJoinRequest> getJoinRequestList(int studyId);
	
	int getJoinRequestCount(int studyId);
}

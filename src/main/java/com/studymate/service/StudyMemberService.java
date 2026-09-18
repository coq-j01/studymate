package com.studymate.service;

import java.util.List;

import com.studymate.domain.StudyJoinRequest;
import com.studymate.domain.StudyMember;

public interface StudyMemberService {
	//가입 승인
	void approveJoinRequest(int studyId, int memberId, int leaderId);
	
	//가입 거절
	void rejectJoinRequest(int studyId, int memberId, int leaderId);
	
	//inactive로 변경
	void inactiveMember(int studyId, int memberId);
	
	List<StudyJoinRequest> getJoinRequestList(int studyId);

	//스터디원 목록
	List<StudyMember> getStudyMemberList(int studyId);
	
	//board에 접근 가능여부
	boolean canAccessStudy(int studyId, int memberId);
}

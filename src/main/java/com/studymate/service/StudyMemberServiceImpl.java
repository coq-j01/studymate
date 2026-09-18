package com.studymate.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.studymate.domain.StudyJoinRequest;
import com.studymate.domain.StudyMember;
import com.studymate.mapper.StudyJoinRequestMapper;
import com.studymate.mapper.StudyMapper;
import com.studymate.mapper.StudyMemberMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudyMemberServiceImpl implements StudyMemberService {
	private final StudyMapper studyMapper;
	private final StudyMemberMapper studyMemberMapper;
	private final StudyJoinRequestMapper studyJoinRequestMapper;

	@Override
	@Transactional
	public void approveJoinRequest(int studyId, int memberId, int leaderId) {
		// 요청자가 스터디 장인지 확인
		if (!studyMapper.isStudyLeader(studyId, leaderId)) {
			throw new IllegalStateException("스터디장 권한이 없습니다.");
		}
		// 신청서 존재 확인
		if (!studyJoinRequestMapper.existsJoinRequest(studyId, memberId)) {
			throw new IllegalStateException("존재하지 않는 가입 신청입니다.");
		}
		// 신청서가 존재하면 정원 초과 확인 후 등록
		int currentMemberCount = studyMemberMapper.countStudyMembers(studyId);
		Integer maxMember = studyMapper.findMaxMember(studyId);
		if (currentMemberCount >= maxMember) {
			throw new IllegalStateException("스터디 정원이 가득 찼습니다.");
		}
		//멤버 등록
		studyMemberMapper.insertStudyMember(studyId, memberId);
		//신청서 삭제
		studyJoinRequestMapper.deleteJoinRequest(studyId, memberId);
		
		// 이번 승인으로 정원이 가득 찼다면 모집 마감
		if (currentMemberCount + 1 >= maxMember) {
		    studyMapper.closeStudy(studyId);
		}
	}

	@Override
	public void rejectJoinRequest(int studyId, int memberId, int leaderId) {
		// 요청자가 스터디 장인지 확인
		if (!studyMapper.isStudyLeader(studyId, leaderId)) {
			throw new IllegalStateException("스터디장 권한이 없습니다.");
		}
		if (!studyJoinRequestMapper.existsJoinRequest(studyId, memberId)) {
	        throw new IllegalStateException("존재하지 않는 가입 신청입니다.");
	    }
		//신청서 삭제
		studyJoinRequestMapper.deleteJoinRequest(studyId, memberId);
	}

	@Override
	public List<StudyJoinRequest> getJoinRequestList(int studyId) {
		return studyJoinRequestMapper.findJoinRequests(studyId);
	}

	@Override
	public List<StudyMember> getStudyMemberList(int studyId) {
		return studyMemberMapper.findStudyMembers(studyId);
	}

	@Override
	public boolean canAccessStudy(int studyId, int memberId) {
		//study가 end가 아닌지 확인
		if(studyMapper.getStudyStatus(studyId).equals("ENDED")) {
			throw new IllegalStateException("종료된 스터디입니다.");
		}
		//접근자가 active상태인지 확인
		if(!studyMemberMapper.findStudyMemberStatus(studyId, memberId).equals("ACTIVE")) {
			throw new IllegalStateException("접근 불가능한 사용자입니다.");
		}
		return true;
	}

	@Override
	public void inactiveMember(int studyId, int memberId) {
		studyMemberMapper.inactiveStudyMember(studyId, memberId);
	}

}

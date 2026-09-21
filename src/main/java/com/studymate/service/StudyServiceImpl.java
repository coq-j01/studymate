package com.studymate.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.studymate.domain.Study;
import com.studymate.dto.StudyDTO;
import com.studymate.mapper.StudyJoinRequestMapper;
import com.studymate.mapper.StudyMapper;
import com.studymate.mapper.StudyMemberMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudyServiceImpl implements StudyService {
	private final StudyMapper studyMapper;
	private final StudyMemberMapper studyMemberMapper;
	private final StudyJoinRequestMapper studyJoinRequestMapper;

	@Override
	@Transactional
	public int createStudy(StudyDTO studyDTO, int leaderId) {
		int studyResult = studyMapper.insertStudy(studyDTO, leaderId);
		if (studyResult != 1) {
			log.info("스터디 생성에 실패");
			throw new IllegalStateException("스터디 생성에 실패했습니다.");
		}
		int studyId = studyDTO.getStudyId();
		int studyMemberResult = studyMemberMapper.insertLeader(studyId, leaderId);
		if (studyMemberResult != 1) {
			log.info("스터디장 등록에 실패");
			throw new IllegalStateException("스터디장 등록에 실패했습니다.");
		}
		return studyId;
	}

	@Override
	public List<Study> getStudyList(String keyword, int page, int size, Integer categoryId) {
	    int offset = (page - 1) * size;
		return studyMapper.findStudyList(keyword, categoryId, size, offset);
	}

	@Override
	public List<Study> getMyStudyList(int memberId, int page, int size) {
	    int offset = (page - 1) * size;
		return studyMapper.findMyStudyList(memberId, size, offset);
	}

	@Override
	public Study getStudy(int studyId) {
		return studyMapper.findStudyById(studyId);
	}

	@Override
	public String findStudyMemberStatus(int studyId, int memberId) {
		// 1. 실제 스터디원인지 확인
		String status = studyMemberMapper.findStudyMemberStatus(studyId, memberId);

		// ACTIVE / INACTIVE
		if (status != null) {
			return status;
		}
		// 2. 가입 신청서가 있는지 확인
		boolean existRequest = studyJoinRequestMapper.existsJoinRequest(studyId, memberId);

		if (existRequest) {
			return "PENDING";
		}

		// 3. 아무 관계 없음
		return null;
	}

	@Override
	public void updateStudy(StudyDTO studyDTO, int memberId) {
		Study study = studyMapper.findStudyById(studyDTO.getStudyId());

		if (study.getLeaderId() != memberId) {
			throw new IllegalStateException("스터디 수정 권한이 없습니다.");
		}

		int result = studyMapper.updateStudy(studyDTO);

		if (result != 1) {
			throw new IllegalStateException("스터디 수정에 실패했습니다.");
		}
	}

	@Override
	public void applyStudy(int studyId, int memberId, String message) {
	    
	 // 모집 중인지
	    if (!studyMapper.getStudyStatus(studyId).equals("RECRUITING")) {
	        throw new IllegalStateException(
	                "현재 모집 중인 스터디가 아닙니다."
	        );
	    }
	 // 이미 스터디원이거나 탈퇴/강퇴 이력이 있는지
	    String memberStatus = studyMemberMapper.findStudyMemberStatus( studyId,memberId);
		
	    if (memberStatus != null) {
	        throw new IllegalStateException(
	                "신청할 수 없는 스터디입니다."
	        );
	    }
	    
	 // 이미 신청했는지
	    boolean existRequest =
	            studyJoinRequestMapper.existsJoinRequest(
	                    studyId,
	                    memberId
	            );

	    if (existRequest) {
	        throw new IllegalStateException(
	                "이미 신청한 스터디입니다."
	        );
	    }
	 // 정원 확인
	    int currentMemberCount =
	            studyMemberMapper.countStudyMembers(studyId);

	    if (currentMemberCount >= studyMapper.findMaxMember(studyId)) {
	        throw new IllegalStateException(
	                "모집 인원이 가득 찼습니다."
	        );
	    }
	    
	 // 신청서 등록
	    int result =
	            studyJoinRequestMapper.insertJoinRequest(
	                    studyId,
	                    memberId,
	                    message
	            );

	    if (result != 1) {
	        throw new IllegalStateException(
	                "스터디 신청에 실패했습니다."
	        );
	    }
	}

	@Override
	public int getMaxMember(int studyId) {
		return studyMapper.findMaxMember(studyId);
	}

	@Override
	public boolean isLeader(int studyId, int memberId) {
		return studyMapper.isStudyLeader(studyId, memberId);
	}

	@Override
	public int getStudyCount(String keyword, Integer categoryId) {
		return studyMapper.countStudyList(keyword, categoryId);
	}

	@Override
	public int getMyStudyCount(int memberId) {
		return studyMapper.countMyStudyList(memberId);
	}

}

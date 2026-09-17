package com.studymate.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.studymate.domain.Study;
import com.studymate.dto.StudyDTO;
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

	@Override
	@Transactional
	public int createStudy(StudyDTO studyDTO, int leaderId) {
		int studyResult = studyMapper.insertStudy(studyDTO, leaderId);
		if(studyResult != 1) {
			log.info("스터디 생성에 실패");
			throw new IllegalStateException("스터디 생성에 실패했습니다.");
		}
		int studyId = studyDTO.getStudyId();
		int studyMemberResult = studyMemberMapper.insertLeader(studyId, leaderId);
		if(studyMemberResult != 1) {
			log.info("스터디장 등록에 실패");
			throw new IllegalStateException("스터디장 등록에 실패했습니다.");
		}
		return studyId;
	}

	@Override
	public List<Study> getStudyList() {
		return studyMapper.findStudyList();
	}

	@Override
	public List<Study> getMyStudyList(int memberId) {
		return studyMapper.findMyStudyList(memberId);
	}
	
}

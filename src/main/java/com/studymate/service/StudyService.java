package com.studymate.service;

import java.util.List;

import com.studymate.domain.Study;
import com.studymate.dto.StudyDTO;

public interface StudyService {

	int createStudy(StudyDTO studyDTO, int leaderId);
	
	List<Study> getStudyList();
	
	List<Study> getMyStudyList(int memberId);
}

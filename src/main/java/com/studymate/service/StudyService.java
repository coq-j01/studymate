package com.studymate.service;

import com.studymate.dto.StudyDTO;

public interface StudyService {
	int createStudy(StudyDTO studyDTO, int leaderId);
}

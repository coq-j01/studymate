package com.studymate.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.studymate.domain.Study;
import com.studymate.dto.StudyDTO;

public interface StudyService {

	int createStudy(StudyDTO studyDTO, MultipartFile thumbnailFile, int leaderId);
	
	List<Study> getStudyList(String keyword, int page, int size, Integer categoryId);
	
	List<Study> getMyStudyList(int memberId, int page, int size);
	
	Study getStudy(int studyId);
	
	String findStudyMemberStatus(int studyId, int memberId);
	
	void updateStudy(StudyDTO studyDTO, MultipartFile thumbnailFile , int memberId);
	
	void applyStudy(int studyId,int memberId,String message);
	
	int getMaxMember(int studyId);
	
	boolean isLeader(int studyId, int memberId);
	
	int getStudyCount(String keyword, Integer categoryId);
	int getMyStudyCount(int memberId);
	
	int endStudy(int studyId, int memberId);
	int deleteStudy(int studyId, int memberId);
}

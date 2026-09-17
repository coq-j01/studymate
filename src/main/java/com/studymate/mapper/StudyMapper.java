package com.studymate.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.studymate.domain.Study;
import com.studymate.dto.StudyDTO;

@Mapper
public interface StudyMapper {

	//스터디 생성, 성공여부 반환(0:실패, 1:성공)
	int insertStudy(@Param("studyDTO") StudyDTO studyDTO,
		    @Param("leaderId") int leaderId);
	//스터디 목록 조회
	List<Study> findStudyList(@Param("keyword") String keyword, @Param("categoryId") Integer categoryId);
	//스터디 단건 조회
	Study findStudyById(int studyId);
	//스터디 업데이트, 성공여부 반환(0:실패, 1:성공)
	int updateStudy(StudyDTO studyDTO);
	//스터디 상태 종료로 업데이트, 성공여부 반환(0:실패, 1:성공)
	int endStudy(int studyId);
	//스터디 삭제, 성공여부 반환(0:실패, 1:성공)
	//스터디 갯수 조회,페이징 시 사용
	int countStudyList();
	
	//내 스터디 목록 조회
	List<Study> findMyStudyList(int memberId);
	//내 스터디 갯수 조회,페이징 시 사용
	int countMyStudyList(int memberId);
}

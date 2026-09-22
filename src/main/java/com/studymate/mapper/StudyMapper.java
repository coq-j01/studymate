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
	List<Study> findStudyList(@Param("keyword") String keyword, @Param("categoryId") Integer categoryId,
			@Param("size") int size, @Param("offset") int offset);
	//스터디 단건 조회
	Study findStudyById(int studyId);
	//스터디 업데이트, 성공여부 반환(0:실패, 1:성공)
	int updateStudy(StudyDTO studyDTO);
	//스터디 상태 종료로 업데이트, 성공여부 반환(0:실패, 1:성공)
	int endStudy(int studyId);
	//스터디 모집 종료
	int closeStudy(int studyId);
	//스터디 삭제, 성공여부 반환(0:실패, 1:성공)
	int deleteStudy(int studyId);
	//스터디 갯수 조회,페이징 시 사용
	int countStudyList(@Param("keyword") String keyword, @Param("categoryId") Integer categoryId);
	//thumbnail조회
	String getThumbnail(int studyId);
	
	
	//내 스터디 목록 조회
	List<Study> findMyStudyList(@Param("memberId") int memberId, @Param("size") int size, @Param("offset") int offset);
	//내 스터디 갯수 조회,페이징 시 사용
	int countMyStudyList(int memberId);
	
	// 스터디장 여부 확인
	boolean isStudyLeader(
	        @Param("studyId") int studyId,
	        @Param("memberId") int memberId
	);
	//스터디장 조회
	int findLeader(int StudyId);
	//스터디 제목 조회
	String findStudyTitle(int StudyId);
	
	// 스터디 최대 인원 조회
	int findMaxMember(int studyId);
	// 현재 모집 중인 스터디인지 확인
	String getStudyStatus(int studyId);
}

package com.studymate.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.studymate.domain.StudyJoinRequest;

@Mapper
public interface StudyJoinRequestMapper {
	//스터디에 신청한 사용자인지 확인
	boolean existsJoinRequest(@Param("studyId") int studyId,
		    @Param("memberId") int memberId);
	
	//스터디에 신청자 수 조회
	int countJoinRequest(int studyId);
	
	// 신청서 등록
    int insertJoinRequest(
            @Param("studyId") int studyId,
            @Param("memberId") int memberId,
            @Param("message") String message
    );
    //신청자 목록 조회
    List<StudyJoinRequest> findJoinRequests(int studyId);
    
    //승인/거절 시 신청자 목록에서 삭제
    int deleteJoinRequest(@Param("studyId") int studyId,
		    @Param("memberId") int memberId);
}

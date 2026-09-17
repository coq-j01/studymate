package com.studymate.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface StudyJoinRequestMapper {
	//스터디에 신청한 사용자인지 확인
	int countJoinRequest(@Param("studyId") int studyId,
		    @Param("memberId") int memberId);
	
	// 신청서 등록
    int insertJoinRequest(
            @Param("studyId") int studyId,
            @Param("memberId") int memberId,
            @Param("message") String message
    );
}

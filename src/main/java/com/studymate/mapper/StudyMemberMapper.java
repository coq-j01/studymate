package com.studymate.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.studymate.domain.StudyMember;

@Mapper
public interface StudyMemberMapper {
	
	// 스터디 생성 시 스터디장 등록
	int insertLeader(
	    @Param("studyId") int studyId,
	    @Param("memberId") int memberId
	);

	// 일반 회원 가입 신청
	int insertStudyMember(
	    @Param("studyId") int studyId,
	    @Param("memberId") int memberId
	);
	
	//특정 회원의 해당 스터디 상태 조회
    String findStudyMemberStatus(
            @Param("studyId") int studyId,
            @Param("memberId") int memberId
    );

	//스터디원 수 반환
	int countStudyMembers(int studyId);
    
    //탈퇴 / 강퇴
    int inactiveStudyMember(
            @Param("studyId") int studyId,
            @Param("memberId") int memberId
    );
    
    //현재 스터디원 목록
    List<StudyMember> findStudyMembers(int studyId);
    
}
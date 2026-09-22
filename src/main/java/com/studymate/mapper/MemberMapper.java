package com.studymate.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.studymate.domain.Member;
import com.studymate.dto.JoinDTO;

@Mapper
public interface MemberMapper {
	
	//이메일로 회원조회
	Member findByEmail(String email);
	
	//닉네임 중복조회
	Member findByNickname(String nickname);
	
	//회원 추가, 성공여부 반환(0:실패, 1:성공)
	int insertMember(JoinDTO joinDTO);
	
	//회원 이메일 조회
	String findEmailById(int memberId);
}

package com.studymate.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.studymate.domain.Member;
import com.studymate.dto.JoinDTO;
import com.studymate.dto.SocialJoinDTO;

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
	
	//소셜로그인 확인
	Member findByProviderAndProviderId( @Param("provider") String provider, @Param("providerId") String providerId);

	//소셜 로그인 가입
	int insertSocialMember(Member member);
}

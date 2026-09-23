package com.studymate.mapper;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.studymate.domain.StudyPost;
import com.studymate.dto.StudyPostDTO;

@Mapper
public interface StudyPostMapper {
	List<StudyPost> findPostList(@Param("studyId") int studyId, @Param("postType") String postType,
			@Param("limit") int limit, @Param("offset") Integer offset);

	int insertBoard(@Param("studyId") int studyId, @Param("memberId") int memberId, @Param("postType") String postType,
			@Param("studyPostDTO") StudyPostDTO studyPostDTO);

	int updateBoard(@Param("postId") int postId, @Param("studyPostDTO") StudyPostDTO studyPostDTO);

	int deleteBoard(int postId);

	StudyPost findPostById(int postId);

	boolean isLiked(@Param("postId") int postId, @Param("memberId") int memberId);

	// 좋아요
	int insertLiked(@Param("postId") int postId, @Param("memberId") int memberId);

	// 좋아요취소
	int deleteLiked(@Param("postId") int postId, @Param("memberId") int memberId);

	int countPost(@Param("studyId") int studyId, @Param("postType") String postType);

	int countPostByDate(@Param("studyId") int studyId, @Param("memberId") int memberId);

	LocalDateTime findLastPostAt(@Param("studyId") int studyId, @Param("memberId") int memberId);
}
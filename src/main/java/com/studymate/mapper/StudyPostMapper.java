package com.studymate.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.studymate.domain.StudyPost;
import com.studymate.dto.StudyPostDTO;

@Mapper
public interface StudyPostMapper {
	List<StudyPost> findPostList( @Param("studyId") int studyId, 
			@Param("postType") String postType, @Param("limit") Integer limit);
	
	int insertBoard(@Param("studyId") int studyId, @Param("memberId") int memberId, @Param("postType") String postType,
			@Param("studyPostDTO") StudyPostDTO studyPostDTO);
}
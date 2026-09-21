package com.studymate.service;

import java.util.List;

import com.studymate.domain.StudyPost;
import com.studymate.dto.StudyPostDTO;

public interface StudyPostService {
	
	//게시글 리스트 조회
	public List<StudyPost> getPostList(int studyId, String postType, Integer limit);
	
	//게시글 insert
	public int insertPost(int studyId, int memberId, String postType, StudyPostDTO studyPostDTO);
}

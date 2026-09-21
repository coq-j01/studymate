package com.studymate.service;

import java.util.List;

import com.studymate.domain.StudyPost;
import com.studymate.dto.StudyPostDTO;

public interface StudyPostService {
	
	//게시글 리스트 조회
	public List<StudyPost> getPostList(int studyId, String postType, Integer limit);
	
	//게시글 insert
	public int insertPost(int studyId, int memberId, String postType, StudyPostDTO studyPostDTO);
	
	//게시글 update
	public int updatePost(int postId, int memberId, StudyPostDTO studyPostDTO);
	
	//게시글 delete
	public int deletePost(int postId, int memberId);
	
	//게시글 단건 조회
	public StudyPost findPost(int postId);
	
	//사용자가 좋아요 눌렀는지 확인
	public boolean isLiked(int postId, int memberId);
	
	//좋아요 누르기/취소
	public int toggleLike(int postId, int memberId);
}

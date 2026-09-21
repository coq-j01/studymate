package com.studymate.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.studymate.domain.StudyPost;
import com.studymate.dto.StudyPostDTO;
import com.studymate.mapper.StudyMapper;
import com.studymate.mapper.StudyMemberMapper;
import com.studymate.mapper.StudyPostMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudyPostServiceImpl implements StudyPostService {
	private final StudyPostMapper studyPostMapper;
	private final StudyMapper studyMapper;
	private final StudyMemberService studyMemberService;

	@Override
	public List<StudyPost> getPostList(int studyId, String postType, Integer limit) {
		return studyPostMapper.findPostList(studyId, postType, limit);
	}

	@Override
	public int insertPost(int studyId, int memberId, String postType, StudyPostDTO studyPostDTO) {
		if(!studyMemberService.canAccessStudy(studyId, memberId)) {
			throw new IllegalStateException("게시글 작성 권한이 없습니다.");
		}
		if (!"NOTICE".equals(postType)
		        && !"ATTENDANCE".equals(postType)) {
		    throw new IllegalArgumentException("잘못된 게시글 유형입니다.");
		}
		if("NOTICE".equals(postType) && !studyMapper.isStudyLeader(studyId, memberId)) {
			throw new IllegalStateException("스터디장만 작성 가능합니다.");
		}
		return studyPostMapper.insertBoard(studyId, memberId, postType, studyPostDTO);
	}
}
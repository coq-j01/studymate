package com.studymate.service;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.studymate.domain.Study;
import com.studymate.dto.StudyDTO;
import com.studymate.mapper.StudyJoinRequestMapper;
import com.studymate.mapper.StudyMapper;
import com.studymate.mapper.StudyMemberMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudyServiceImpl implements StudyService {
	private final StudyMapper studyMapper;
	private final StudyMemberMapper studyMemberMapper;
	private final StudyJoinRequestMapper studyJoinRequestMapper;
	private final S3FileService s3FileService;

	@Override
	@Transactional
	public int createStudy(StudyDTO studyDTO, MultipartFile thumbnailFile, int leaderId) {
		String thumbnail;
		if (thumbnailFile != null && !thumbnailFile.isEmpty()) {

			// 사용자가 업로드한 이미지 저장
			thumbnail = s3FileService.uploadImage(thumbnailFile);

		} else {

			// 업로드하지 않았으면 기본 이미지 랜덤
			thumbnail = getRandomThumbnail();
		}

		studyDTO.setThumbnail(thumbnail);

		int studyResult = studyMapper.insertStudy(studyDTO, leaderId);
		if (studyResult != 1) {
			log.info("스터디 생성에 실패");
			throw new IllegalStateException("스터디 생성에 실패했습니다.");
		}
		int studyId = studyDTO.getStudyId();
		int studyMemberResult = studyMemberMapper.insertLeader(studyId, leaderId);
		if (studyMemberResult != 1) {
			log.info("스터디장 등록에 실패");
			throw new IllegalStateException("스터디장 등록에 실패했습니다.");
		}
		return studyId;
	}

	@Override
	public List<Study> getStudyList(String keyword, int page, int size, Integer categoryId) {
		int offset = (page - 1) * size;
		List<Study> studyList = studyMapper.findStudyList(keyword, categoryId, size, offset);
		for (Study study : studyList) {

		    study.setThumbnailUrl(
		        s3FileService.getImageUrl(
		            study.getThumbnail()
		        )
		    );
		}
		return studyList;
	}

	@Override
	public List<Study> getMyStudyList(int memberId, int page, int size) {
		int offset = (page - 1) * size;
		List<Study> studyList =  studyMapper.findMyStudyList(memberId, size, offset);
		
		for (Study study : studyList) {

		    study.setThumbnailUrl(
		        s3FileService.getImageUrl(
		            study.getThumbnail()
		        )
		    );
		}
		return studyList;
	}

	@Override
	public Study getStudy(int studyId) {
		Study study = studyMapper.findStudyById(studyId);
		study.setThumbnailUrl(s3FileService.getImageUrl(study.getThumbnail()));
		return study;
	}

	@Override
	public String findStudyMemberStatus(int studyId, int memberId) {
		// 1. 실제 스터디원인지 확인
		String status = studyMemberMapper.findStudyMemberStatus(studyId, memberId);

		// ACTIVE / INACTIVE
		if (status != null) {
			return status;
		}
		// 2. 가입 신청서가 있는지 확인
		boolean existRequest = studyJoinRequestMapper.existsJoinRequest(studyId, memberId);

		if (existRequest) {
			return "PENDING";
		}

		// 3. 아무 관계 없음
		return null;
	}

	@Override
	public void updateStudy(StudyDTO studyDTO, MultipartFile thumbnailFile , int memberId) {
		Study study = studyMapper.findStudyById(studyDTO.getStudyId());

		if (study.getLeaderId() != memberId) {
			throw new IllegalStateException("스터디 수정 권한이 없습니다.");
		}
		String oldThumbnail = study.getThumbnail();
		String newThumbnail = null;
		boolean imageChanged = thumbnailFile != null && !thumbnailFile.isEmpty();
		
		if (imageChanged) {
			// 사용자가 업로드한 이미지 저장
			newThumbnail = s3FileService.uploadImage(thumbnailFile);
			studyDTO.setThumbnail(newThumbnail);
		}else {

	        // 이미지 수정 안 함 → 기존 이미지 유지
	        studyDTO.setThumbnail(study.getThumbnail());
	    }
		int result = studyMapper.updateStudy(studyDTO);

		if (result != 1) {
			s3FileService.deleteImage(newThumbnail);
			throw new IllegalStateException("스터디 수정에 실패했습니다.");
		}
		if (imageChanged) {
	        s3FileService.deleteImage(oldThumbnail);
	    }
	}

	@Override
	public void applyStudy(int studyId, int memberId, String message) {

		// 모집 중인지
		if (!studyMapper.getStudyStatus(studyId).equals("RECRUITING")) {
			throw new IllegalStateException("현재 모집 중인 스터디가 아닙니다.");
		}
		// 이미 스터디원이거나 탈퇴/강퇴 이력이 있는지
		String memberStatus = studyMemberMapper.findStudyMemberStatus(studyId, memberId);

		if (memberStatus != null) {
			throw new IllegalStateException("신청할 수 없는 스터디입니다.");
		}

		// 이미 신청했는지
		boolean existRequest = studyJoinRequestMapper.existsJoinRequest(studyId, memberId);

		if (existRequest) {
			throw new IllegalStateException("이미 신청한 스터디입니다.");
		}
		// 정원 확인
		int currentMemberCount = studyMemberMapper.countStudyMembers(studyId);

		if (currentMemberCount >= studyMapper.findMaxMember(studyId)) {
			throw new IllegalStateException("모집 인원이 가득 찼습니다.");
		}

		// 신청서 등록
		int result = studyJoinRequestMapper.insertJoinRequest(studyId, memberId, message);

		if (result != 1) {
			throw new IllegalStateException("스터디 신청에 실패했습니다.");
		}
	}

	@Override
	public int getMaxMember(int studyId) {
		return studyMapper.findMaxMember(studyId);
	}

	@Override
	public boolean isLeader(int studyId, int memberId) {
		return studyMapper.isStudyLeader(studyId, memberId);
	}

	@Override
	public int getStudyCount(String keyword, Integer categoryId) {
		return studyMapper.countStudyList(keyword, categoryId);
	}

	@Override
	public int getMyStudyCount(int memberId) {
		return studyMapper.countMyStudyList(memberId);
	}

	@Override
	public int endStudy(int studyId, int memberId) {
		if (!studyMapper.isStudyLeader(studyId, memberId)) {
			throw new IllegalStateException("스터디 종료 권한이 없습니다.");
		}
		return studyMapper.endStudy(studyId);
	}

	@Override
	public int deleteStudy(int studyId, int memberId) {
		if (!studyMapper.isStudyLeader(studyId, memberId)) {
			throw new IllegalStateException("스터디 삭제 권한이 없습니다.");
		}
		// 여기서 스터디원 수 확인
		if (studyMemberMapper.countStudyMembers(studyId) > 1) {
			throw new IllegalStateException("스터디원이 존재하여 삭제할 수 없습니다.");
		}
		String thumbnail = studyMapper.getThumbnail(studyId);
		int result = studyMapper.deleteStudy(studyId);
		if(result == 1 ) {
			s3FileService.deleteImage(thumbnail);
		}
		return result;
	}
	
	//랜덤 썸네일 지정
	private String getRandomThumbnail() {

	    int randomNumber =
	        ThreadLocalRandom.current().nextInt(1, 9);

	    return "defaults/thumbnail_"
	            + randomNumber
	            + ".png";
	}

	@Override
	public int getLeaderId(int studyId) {
		return studyMapper.findLeader(studyId);
	}

	@Override
	public String getTitle(int studyId) {
		return studyMapper.findStudyTitle(studyId);
	}
	
}

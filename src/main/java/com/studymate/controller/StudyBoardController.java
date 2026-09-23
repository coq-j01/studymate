package com.studymate.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.studymate.domain.StudyPost;
import com.studymate.dto.StudyPostDTO;
import com.studymate.security.CustomUserDetails;
import com.studymate.service.StudyMemberService;
import com.studymate.service.StudyPostService;
import com.studymate.service.StudyService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/study/{studyId}")
public class StudyBoardController {
	private final StudyService studyService;
	private final StudyMemberService studyMemberService;
	private final StudyPostService studyPostService;

	@ModelAttribute
	public void addStudy(@PathVariable int studyId, Model model) {

		model.addAttribute("study", studyService.getStudy(studyId));
	}

	@GetMapping("/home")
	public String home(@PathVariable int studyId, @AuthenticationPrincipal CustomUserDetails userDetails, Model model) {

		if (!studyMemberService.canAccessStudy(studyId, userDetails.getMemberId())) {
			return "redirect:/study/main";
		}
		model.addAttribute("recentNoticeList", studyPostService.getPostList(studyId, "NOTICE", 3, null));
		model.addAttribute("recentAttendnaceList", studyPostService.getPostList(studyId, "ATTENDANCE", 3, null));

		return "study/board/home";
	}

	@GetMapping("/notice")
	public String notice(@PathVariable int studyId, @RequestParam(defaultValue = "1") int page,
			@AuthenticationPrincipal CustomUserDetails userDetails,
			Model model) {

		if (!studyMemberService.canAccessStudy(studyId, userDetails.getMemberId())) {

			return "redirect:/study/main";
		}
		int pageSize = 10;
		 // 전체 게시글 수
	    int totalCount = studyPostService.countPost(studyId, "NOTICE");

	    int totalPages = (int) Math.ceil(
	            (double) totalCount / pageSize
	    );

		model.addAttribute("noticeList", studyPostService.getPostList(studyId, "NOTICE", pageSize, page));
		model.addAttribute("currentPage", page);
	    model.addAttribute("totalPages", totalPages);
	    
		return "study/board/notice";
	}

	@GetMapping("/attendance")
	public String attendance(@PathVariable int studyId,@RequestParam(defaultValue = "1") int page,
			@AuthenticationPrincipal CustomUserDetails userDetails,
			Model model) {

		if (!studyMemberService.canAccessStudy(studyId, userDetails.getMemberId())) {

			return "redirect:/study/main";
		}

		int pageSize = 10;
		 // 전체 게시글 수
		int totalCount = studyPostService.countPost(studyId, "ATTENDANCE");

	    int totalPages = (int) Math.ceil(
	            (double) totalCount / pageSize
	    );
		
		model.addAttribute("attendanceList", studyPostService.getPostList(studyId, "ATTENDANCE", pageSize,page));
		model.addAttribute("currentPage", page);
	    model.addAttribute("totalPages", totalPages);
	    
		return "study/board/attendance";
	}

	@GetMapping("/members")
	public String getMembers(@PathVariable int studyId, @AuthenticationPrincipal CustomUserDetails userDetails,
			Model model) {
		if (!studyMemberService.canAccessStudy(studyId, userDetails.getMemberId())) {

			return "redirect:/study/main";
		}

		model.addAttribute("memberList", studyMemberService.getStudyMemberList(studyId));

		model.addAttribute("maxMember", studyService.getMaxMember(studyId));
		model.addAttribute("loginId", userDetails.getMemberId());

		return "study/board/members";
	}

	@GetMapping("/{type}/write")
	public String getWrite(@PathVariable int studyId, @PathVariable String type,
			@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
		if (!studyMemberService.canAccessStudy(studyId, userDetails.getMemberId())) {

			return "redirect:/study/main";
		}

		// 공지는 스터디장만 작성 가능
		if (type.equals("notice") && !studyService.isLeader(studyId, userDetails.getMemberId())) {

			return "redirect:/study/" + studyId + "/notice";
		}

		model.addAttribute("writeType", type);

		model.addAttribute("postTypeName", type.equals("notice") ? "공지 게시글" : "인증 게시글");
		return "study/board/write";
	}

	@PostMapping("/{type}/write")
	public String postWrite(@PathVariable int studyId, @PathVariable String type ,
			StudyPostDTO studyPostDTO, @AuthenticationPrincipal CustomUserDetails userDetails) {
		studyPostService.insertPost(studyId, userDetails.getMemberId(), type.toUpperCase(), studyPostDTO);
		return "redirect:/study/"+studyId+"/"+type;
	}

	@GetMapping("/board/{postId}")
	public String postDetail(@PathVariable int studyId, @PathVariable int postId,
			@AuthenticationPrincipal CustomUserDetails userDetails,
	                         Model model) {
		if (!studyMemberService.canAccessStudy(studyId, userDetails.getMemberId())) {

			return "redirect:/study/main";
		}

	    StudyPost post = studyPostService.findPost(postId);

	    int memberId = userDetails.getMemberId(); // 로그인 사용자 memberId
	    boolean liked = studyPostService.isLiked(postId, memberId);

	    model.addAttribute("post", post);
	    model.addAttribute("liked", liked);

	    return "study/board/board-detail";
	}
	
	@PostMapping("/board/{postId}/like")
	public String postLike(@PathVariable int studyId, @PathVariable int postId,
			@AuthenticationPrincipal CustomUserDetails userDetails) {
		studyPostService.toggleLike(postId, userDetails.getMemberId());
		return "redirect:/study/" + studyId + "/board/" + postId;
	}
	@GetMapping("/board/{postId}/edit")
	public String getEdit(@PathVariable int studyId, @PathVariable int postId,
			Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
		StudyPost post = studyPostService.findPost(postId);
		if (!studyMemberService.canAccessStudy(studyId, userDetails.getMemberId())) {
			return "redirect:/study/main";
		}
		if(post.getMemberId() != userDetails.getMemberId()) {
			return "redirect:/study/" + studyId + "/board/" + postId;
		}
		model.addAttribute("post", post);
		return "study/board/board-edit";
	}
	@PostMapping("/board/{postId}/edit")
	public String postEdit(@PathVariable int studyId, @PathVariable int postId,
			@AuthenticationPrincipal CustomUserDetails userDetails, StudyPostDTO studyPostDTO) {
		studyPostService.updatePost(postId, userDetails.getMemberId(), studyPostDTO);
		return "redirect:/study/" + studyId + "/board/" + postId;
	}
	@PostMapping("/board/{postId}/delete")
	public String deleteEdit(@PathVariable int studyId, @PathVariable int postId,
			@AuthenticationPrincipal CustomUserDetails userDetails, StudyPostDTO studyPostDTO) {
		studyPostService.deletePost(postId, userDetails.getMemberId());
		return "redirect:/study/" + studyId + "/home";
	}

}

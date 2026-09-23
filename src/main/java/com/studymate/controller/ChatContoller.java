 package com.studymate.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import com.studymate.dto.ChatMessageDTO;
import com.studymate.security.CustomUserDetails;
import com.studymate.service.ChatService;
import com.studymate.service.StudyMemberService;
import com.studymate.service.StudyService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ChatContoller {
	private final ChatService chatService;
	private final StudyService studyService;
	private final StudyMemberService studyMemberService;
	
	@ModelAttribute
	public void addStudy(@PathVariable int studyId, Model model) {

		model.addAttribute("study", studyService.getStudy(studyId));
	}

	@MessageMapping("/study/{studyId}/chat")
    @SendTo("/topic/study/{studyId}")
    public ChatMessageDTO sendMessage(
            @DestinationVariable int studyId,
            Principal principal,
            ChatMessageDTO chatMessageDTO) {
		
		//websocket연결 사용자 정보를 Spring Security의 로그인 정보 객체로 바꿈
		Authentication authentication = (Authentication) principal;
		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

		if (!studyMemberService.canAccessStudy(studyId, userDetails.getMemberId())) {
			return null;
		}
		
		chatMessageDTO.setStudyId(studyId);
		chatMessageDTO.setSendAt(LocalDateTime.now());
        
		chatMessageDTO.setMemberId(userDetails.getMemberId());
		chatMessageDTO.setNickname(userDetails.getNickname());
        
        chatService.saveChat(chatMessageDTO);

        return chatMessageDTO;
    }
	@GetMapping("/study/{studyId}/chat")
	public String getChat(
	        @PathVariable int studyId,
	        @AuthenticationPrincipal CustomUserDetails userDetails,
	        Model model) {
		if (!studyMemberService.canAccessStudy(studyId, userDetails.getMemberId())) {
			return "redirect:/study/main";
		}

	    List<ChatMessageDTO> chatList = chatService.getChat(studyId);

	    model.addAttribute("chatList", chatList);

	    return "study/board/chat";
	}
}

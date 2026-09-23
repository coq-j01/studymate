package com.studymate.service;

import java.util.List;

import com.studymate.dto.ChatMessageDTO;

public interface ChatService {
	void saveChat(ChatMessageDTO chatMessageDTO);
	
	List<ChatMessageDTO> getChat(int studyId);
}

package com.studymate.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.studymate.dto.ChatMessageDTO;
import com.studymate.mapper.ChatMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
	private final ChatMapper chatMapper;
	
	@Override
	public void saveChat(ChatMessageDTO chatMessageDTO) {
		chatMapper.insertChat(chatMessageDTO);
	}

	@Override
	public List<ChatMessageDTO> getChat(int studyId) {
		return chatMapper.findChatList(studyId);
	}

}

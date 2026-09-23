package com.studymate.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.studymate.dto.ChatMessageDTO;

@Mapper
public interface ChatMapper {
	void insertChat(ChatMessageDTO chatMessageDTO);
	List<ChatMessageDTO> findChatList(int studyId);
}

package com.studymate.controller;

import java.time.LocalDateTime;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.studymate.dto.ChatMessageDTO;

@Controller
public class ChatContoller {

	@MessageMapping("/study/{studyId}/chat")
    @SendTo("/topic/study/{studyId}")
    public ChatMessageDTO sendMessage(
            @DestinationVariable int studyId,
            ChatMessageDTO message) {

        message.setStudyId(studyId);
        message.setSentAt(LocalDateTime.now());

        return message;
    }
}

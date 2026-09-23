package com.studymate.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ChatMessageDTO {

    private int studyId;
    private int memberId;
    private String nickname;
    private String message;
    private LocalDateTime sentAt;
}

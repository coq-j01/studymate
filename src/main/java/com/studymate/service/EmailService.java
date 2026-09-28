package com.studymate.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
    @Async
    public void sendEmail(String to, String subject, String text, String link) {
    	try {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(to);
        message.setSubject(subject);
        String content = text;

        if (link != null && !link.isBlank()) {
            content += "\n\n"
                    + "StudyMate 바로가기: "
                    + link;
        }

        message.setText(content);
        mailSender.send(message);
    	}catch(Exception e) {
    		log.error(
                    "이메일 발송 실패 - to: {}, subject: {}",
                    to,
                    subject,
                    e
                );
    	}
    }
}
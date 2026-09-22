package com.studymate.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String to, String subject, String text, String link) {

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
    }
}
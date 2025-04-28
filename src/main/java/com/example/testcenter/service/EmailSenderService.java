package com.example.testcenter.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class EmailSenderService {

    @Autowired
    private JavaMailSender mailSender;


    public void sendEmail(String toEmail,String subject, String text){
        SimpleMailMessage message = new SimpleMailMessage();
      //  message.setFrom("test-centre@example.com");
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(text);
        message.setReplyTo("test-centre@example.com");

        mailSender.send(message);
        log.info("------ email sent successfully");
    }
}

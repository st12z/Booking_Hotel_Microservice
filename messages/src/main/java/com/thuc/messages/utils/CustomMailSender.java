package com.thuc.messages.utils;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomMailSender {
    private final Logger logger = LoggerFactory.getLogger(CustomMailSender.class);
    private final JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String username;
    public  void  sendMail(String email,String content,String subject)  {
        try{
            logger.debug("Sending mail to {}", email);
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
            helper.setFrom(username);
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(content, true);
            mailSender.send(mimeMessage);
        }catch (Exception e){
            throw new RuntimeException(e);
        }

    }
}

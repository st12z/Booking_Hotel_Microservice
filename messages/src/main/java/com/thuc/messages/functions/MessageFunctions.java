package com.thuc.messages.functions;

import com.thuc.messages.dto.UserDto;
import com.thuc.messages.utils.CustomMailSender;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
@RequiredArgsConstructor
public class MessageFunctions {
    private static final Logger log = LoggerFactory.getLogger(MessageFunctions.class);
    private final CustomMailSender mailSender;
    @Bean
    public Function<UserDto, String> sendEmail(){
        return userDto->{
            log.debug("send email with userDto={}", userDto.toString());
            String subject = "Tạo tài khoản";
            String content = "<h3>Tạo tại khoản thành công</h3>" +
                    "<h3>Email:" +userDto.email()+"<h3/>"+
                    "<h3>Mật khẩu:"+userDto.password()+"<h3/>"
                    ;
            mailSender.sendMail(userDto.email(),content,subject);
            return userDto.email();
        };
    }
}

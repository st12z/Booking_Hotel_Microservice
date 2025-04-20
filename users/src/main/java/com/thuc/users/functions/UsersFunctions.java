package com.thuc.users.functions;

import com.thuc.users.dto.requestDto.UserRequestDto;
import com.thuc.users.dto.responseDto.UserDto;
import com.thuc.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
@RequiredArgsConstructor
public class UsersFunctions {
    private static final Logger log = LoggerFactory.getLogger(UsersFunctions.class);
    private final UserRepository userRepository;
    @Bean
    public Consumer<String> updateCommunication(){
        return email-> {
            log.debug("accept email with email={}", email);
            if(email!=null && !email.isEmpty()){
                userRepository.updateSendEmailByEmail(email);
            }
        };
    }
}

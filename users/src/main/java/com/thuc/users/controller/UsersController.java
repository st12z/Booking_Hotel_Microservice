package com.thuc.users.controller;

import com.thuc.users.constant.UsersConstant;
import com.thuc.users.dto.requestDto.UserRequestDto;
import com.thuc.users.dto.responseDto.SuccessResponseDto;
import com.thuc.users.dto.responseDto.UserDto;
import com.thuc.users.service.IUsersService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UsersController {
    private final Logger log = LoggerFactory.getLogger(UsersController.class);
    private final IUsersService usersService;
    @PostMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody @Valid UserRequestDto user) {
        log.debug("Creating use with request: {}", user);
        UserDto userDto = usersService.createUser(user);
        SuccessResponseDto response = SuccessResponseDto.builder()
                .code(UsersConstant.STATUS_201)
                .data(userDto)
                .message(UsersConstant.MESSAGE_201)
                .build();
        return ResponseEntity.ok(response);
    }
}

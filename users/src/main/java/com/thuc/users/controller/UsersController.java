package com.thuc.users.controller;

import com.thuc.users.constant.UsersConstant;
import com.thuc.users.dto.requestDto.UserRequestDto;
import com.thuc.users.dto.responseDto.SuccessResponseDto;
import com.thuc.users.dto.responseDto.UserDto;
import com.thuc.users.service.IUsersService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Map;

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
    @GetMapping("/access-token")
    public ResponseEntity<?> getAccessToken(@RequestParam String code, HttpServletResponse response) {
        log.debug("authorization code received: {}", code);
        Map<String,String> result = usersService.getToken(code);
        Cookie cookie = new Cookie("refresh_token", result.get("refresh_token"));
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(3600*24);
        response.addCookie(cookie);
        SuccessResponseDto success = SuccessResponseDto.builder()
                .code(UsersConstant.STATUS_200)
                .message(UsersConstant.MESSAGE_200)
                .data(result)
                .build();
        return ResponseEntity.ok(success);
    }
    @GetMapping("/refresh-token")
    public ResponseEntity<?> getAccessTokenByRefresh() {
        log.debug("Getting access token by refresh token");
        SuccessResponseDto success = SuccessResponseDto.builder()
                .code(UsersConstant.STATUS_200)
                .message(UsersConstant.MESSAGE_200)
                .data(usersService.getAccessTokenByRefresh())
                .build();
        return ResponseEntity.ok(success);
    }
}

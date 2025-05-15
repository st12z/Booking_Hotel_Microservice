package com.thuc.users.controller;

import com.thuc.users.constant.UsersConstant;
import com.thuc.users.dto.requestDto.RoomChatsDto;
import com.thuc.users.dto.requestDto.UserRequestDto;
import com.thuc.users.dto.responseDto.ErrorResponseDto;
import com.thuc.users.dto.responseDto.SuccessResponseDto;
import com.thuc.users.dto.responseDto.UserDto;
import com.thuc.users.service.IUsersService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UsersController {
    private final Logger log = LoggerFactory.getLogger(UsersController.class);
    private final IUsersService usersService;
    @PostMapping("/register")
    public ResponseEntity<SuccessResponseDto<UserDto>> createUser(@RequestBody @Valid UserRequestDto user) {
        log.debug("Creating use with request: {}", user);
        UserDto userDto = usersService.createUser(user);
        SuccessResponseDto<UserDto> response = SuccessResponseDto.<UserDto>builder()
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
        SuccessResponseDto<?> success = SuccessResponseDto.builder()
                .code(UsersConstant.STATUS_200)
                .message(UsersConstant.MESSAGE_200)
                .data(result)
                .build();
        return ResponseEntity.ok(success);
    }
    @GetMapping("/refresh-token")
    public ResponseEntity<?> getAccessTokenByRefresh() {
        log.debug("Getting access token by refresh token");

        try{
            SuccessResponseDto<?> success = SuccessResponseDto.builder()
                    .code(UsersConstant.STATUS_200)
                    .message(UsersConstant.MESSAGE_200)
                    .data(usersService.getAccessTokenByRefresh())
                    .build();
            return ResponseEntity.ok(success);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponseDto.builder()
                            .code(UsersConstant.STATUS_404)
                            .error(e.getMessage())
                            .build());
        }
    }
    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        log.debug("Logging out");
        Cookie cookie = new Cookie("refresh_token", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        usersService.logout();
        SuccessResponseDto<?> success = SuccessResponseDto.builder()
                .code(UsersConstant.STATUS_200)
                .message("Đăng xuất thành công!")
                .build();
        return ResponseEntity.ok(success);
    }
    @GetMapping("/info-user")
    public ResponseEntity<?> getUserInfo(@RequestHeader("X-User-Email") String email) {
        log.debug("Getting user info");
        UserDto user = usersService.getUserByEmail(email);
        SuccessResponseDto<?> success = SuccessResponseDto.builder()
                .message(UsersConstant.MESSAGE_200)
                .code(UsersConstant.STATUS_200)
                .data(user).build();
        return ResponseEntity.ok(success);
    }
    @GetMapping("/get-user/{id}")
    public ResponseEntity<SuccessResponseDto<UserDto>> getInfoUserById(@PathVariable("id") Integer id) {
        log.debug("Getting user info with id: {}", id);
        SuccessResponseDto<UserDto> response = SuccessResponseDto.<UserDto>builder()
                .code(UsersConstant.STATUS_200)
                .message(UsersConstant.MESSAGE_200)
                .data(usersService.getInfoUserById(id))
                .build();
        return ResponseEntity.ok(response);
    }
    @PostMapping("/create-rooms")
    public ResponseEntity<SuccessResponseDto<RoomChatsDto>> createRoomChats(@RequestBody  RoomChatsDto roomChats) {
        log.debug("Creating room chats with request: {}", roomChats);
        SuccessResponseDto<RoomChatsDto> response = SuccessResponseDto.<RoomChatsDto>builder()
                .code(UsersConstant.STATUS_200)
                .message(UsersConstant.MESSAGE_200)
                .data(usersService.createRoomChats(roomChats))
                .build();
        return ResponseEntity.ok(response);
    }
}

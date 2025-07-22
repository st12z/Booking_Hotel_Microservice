package com.thuc.users.controller;

import com.thuc.users.constant.UsersConstant;
import com.thuc.users.dto.requestDto.FilterUserDto;
import com.thuc.users.dto.requestDto.RoomChatRequestDto;
import com.thuc.users.dto.requestDto.UserRequestDto;
import com.thuc.users.dto.responseDto.*;
import com.thuc.users.service.IUsersService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
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
    @PostMapping("/create-staff")
    public ResponseEntity<SuccessResponseDto<UserDto>> createStaff(@RequestBody @Valid UserRequestDto user) {
        log.debug("Creating use with request: {}", user);
        SuccessResponseDto<UserDto> response = SuccessResponseDto.<UserDto>builder()
                .code(UsersConstant.STATUS_201)
                .data(usersService.createStaff(user))
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
    public ResponseEntity<SuccessResponseDto<UserDto>> getUserInfo(@RequestHeader("X-User-Email") String email) {
        log.debug("Getting user info");
        UserDto userDto = usersService.getUserByEmail(email);
        log.debug("userDto: {}", userDto.toString());
        SuccessResponseDto<UserDto> success = SuccessResponseDto.<UserDto>builder()
                .message(UsersConstant.MESSAGE_200)
                .code(UsersConstant.STATUS_200)
                .data(userDto).build();
        return ResponseEntity.ok(success);
    }
    @GetMapping("/get-user/{id}")
    public ResponseEntity<SuccessResponseDto<UserDto>> getInfoUserById(@PathVariable("id") Integer id) {
        log.debug("Getting user info with id: {}", id);
        UserDto userDto = usersService.getInfoUserById(id);
        SuccessResponseDto<UserDto> response = SuccessResponseDto.<UserDto>builder()
                .code(UsersConstant.STATUS_200)
                .message(UsersConstant.MESSAGE_200)
                .data(userDto)
                .build();
        return ResponseEntity.ok(response);
    }
    @PostMapping("/create-rooms")
    public ResponseEntity<SuccessResponseDto<RoomChatRequestDto>> createRoomChats(@RequestBody RoomChatRequestDto roomChats) {
        log.debug("Creating room chats with request: {}", roomChats);
        SuccessResponseDto<RoomChatRequestDto> response = SuccessResponseDto.<RoomChatRequestDto>builder()
                .code(UsersConstant.STATUS_200)
                .message(UsersConstant.MESSAGE_200)
                .data(usersService.createRoomChats(roomChats))
                .build();
        return ResponseEntity.ok(response);
    }
    @GetMapping("/update-visits")
    public ResponseEntity<SuccessResponseDto<Integer>> updateVisits(@RequestParam(required = false) Integer userId){
        log.debug("Updating visits with request: {}", userId);
        SuccessResponseDto<Integer> response = SuccessResponseDto.<Integer>builder()
                .code(UsersConstant.STATUS_200)
                .message(UsersConstant.MESSAGE_200)
                .data(usersService.updateUserVisits(userId))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @GetMapping("/amount-visits-today")
    public ResponseEntity<SuccessResponseDto<Integer>> amountVisits(){
        log.debug("Getting amount visits");
        SuccessResponseDto<Integer> response = SuccessResponseDto.<Integer>builder()
                .code(UsersConstant.STATUS_200)
                .message(UsersConstant.MESSAGE_200)
                .data(usersService.getAmountVisits())
                .build();
        return ResponseEntity.ok(response);
    }
    @GetMapping("/amount-users")
    public ResponseEntity<SuccessResponseDto<Integer>> getAmountUsers(){
        log.debug("Getting amount users");
        SuccessResponseDto<Integer> response = SuccessResponseDto.<Integer>builder()
                .code(UsersConstant.STATUS_200)
                .message(UsersConstant.MESSAGE_200)
                .data(usersService.getAmountUsers())
                .build();
        return ResponseEntity.ok(response);
    }
    @GetMapping("/amount-visits-month")
    public ResponseEntity<SuccessResponseDto<List<StatisticVisitByMonth>>> getAmountVisitsMonth(@RequestParam Integer month){
        log.debug("Getting amount visits month {}", month);
        SuccessResponseDto<List<StatisticVisitByMonth>> response = SuccessResponseDto.<List<StatisticVisitByMonth>>builder()
                .code(UsersConstant.STATUS_200)
                .message(UsersConstant.MESSAGE_200)
                .data(usersService.getAmountVisitsByMonth(month))
                .build();
        return ResponseEntity.ok(response);
    }
    @GetMapping("/all-users-admin")
    public ResponseEntity<SuccessResponseDto<List<UserDto>>> getAllUsersAdmin(){
        log.debug("Getting all users");
        SuccessResponseDto<List<UserDto>> response = SuccessResponseDto.<List<UserDto>>builder()
                .code(UsersConstant.STATUS_200)
                .message(UsersConstant.MESSAGE_200)
                .data(usersService.getAllUsersAdmin())
                .build();
        return ResponseEntity.ok(response);
    }
    @PostMapping("/filter")
    public ResponseEntity<SuccessResponseDto<PageResponseDto<List<UserDto>>>> filter(@RequestBody FilterUserDto filterDto) throws ParseException {
        log.debug("filter user: {}", filterDto);
        SuccessResponseDto<PageResponseDto<List<UserDto>>> response = SuccessResponseDto.<PageResponseDto<List<UserDto>>>builder()
                .code(UsersConstant.STATUS_200)
                .message(UsersConstant.MESSAGE_200)
                .data(usersService.getAllUsersByPage(filterDto))
                .build();
        return ResponseEntity.ok(response);
    }
    @GetMapping("/search")
    public ResponseEntity<SuccessResponseDto<PageResponseDto<List<UserDto>>>> search(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "1") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize
    ){
        log.debug("Searching for users with keyword: {}", keyword);
        SuccessResponseDto<PageResponseDto<List<UserDto>>> response = SuccessResponseDto.<PageResponseDto<List<UserDto>>>builder()
                .code(UsersConstant.STATUS_200)
                .message(UsersConstant.MESSAGE_200)
                .data(usersService.getSearchUsers(keyword,pageNo,pageSize))
                .build();
        return ResponseEntity.ok(response);
    }
    @PostMapping("/update-roles/{id}")
    public ResponseEntity<SuccessResponseDto<UserDto>> updateRoles(@PathVariable Integer id,@RequestBody List<Integer> roleIds){
        log.debug("Updating roles with request: {}", id);
        SuccessResponseDto<UserDto> response = SuccessResponseDto.<UserDto>builder()
                .code(UsersConstant.STATUS_200)
                .message(UsersConstant.MESSAGE_200)
                .data(usersService.updateRolesByUser(id,roleIds))
                .build();
        return ResponseEntity.ok(response);
    }
    @GetMapping("/reset-password/{id}")
    public ResponseEntity<SuccessResponseDto<UserDto>> resetPassword(@PathVariable Integer id){
        log.debug("Reset password with request: {}", id);
        SuccessResponseDto<UserDto> response = SuccessResponseDto.<UserDto>builder()
                .code(UsersConstant.STATUS_200)
                .message(UsersConstant.MESSAGE_200)
                .data(usersService.resetPassword(id))
                .build();
        return ResponseEntity.ok(response);
    }
}
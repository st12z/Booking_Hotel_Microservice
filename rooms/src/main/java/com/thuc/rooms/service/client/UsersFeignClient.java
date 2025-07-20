package com.thuc.rooms.service.client;

import com.thuc.rooms.dto.SuccessResponseDto;
import com.thuc.rooms.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;


@FeignClient(name = "users",contextId = "usersClient",path = "/api/users")
public interface UsersFeignClient {
    @GetMapping("/info-user")
    public ResponseEntity<SuccessResponseDto<UserDto>> getUserInfo(@RequestHeader("X-User-Email") String email);
    @GetMapping("/get-user/{id}")
    public ResponseEntity<SuccessResponseDto<UserDto>> getInfoUserById(@PathVariable("id") Integer id) ;
    @GetMapping("/all-users-admin")
    public ResponseEntity<SuccessResponseDto<List<UserDto>>> getAllUsersAdmin();
}

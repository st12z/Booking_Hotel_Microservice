package com.thuc.payments.service.client;

import com.thuc.payments.dto.SuccessResponseDto;
import com.thuc.payments.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name="users",path = "/api/users")
public interface UsersFeignClient {
    @GetMapping("/info-user")
    public ResponseEntity<SuccessResponseDto<UserDto>> getUserInfo(@RequestHeader("X-User-Email") String email);
}

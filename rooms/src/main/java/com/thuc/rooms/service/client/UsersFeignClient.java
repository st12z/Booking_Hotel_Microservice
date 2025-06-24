package com.thuc.rooms.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "users",contextId = "usersClient",path = "/api/users")
public interface UsersFeignClient {
    @GetMapping("/info-user")
    public ResponseEntity<?> getUserInfo(@RequestHeader("X-User-Email") String email);
}

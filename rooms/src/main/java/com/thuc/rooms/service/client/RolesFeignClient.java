package com.thuc.rooms.service.client;

import com.thuc.rooms.dto.RoleDto;
import com.thuc.rooms.dto.SuccessResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "users",contextId = "rolesClient",path = "/api/roles")
public interface RolesFeignClient {
    @GetMapping("")
    public ResponseEntity<SuccessResponseDto<List<RoleDto>>> getAllRoles();
}

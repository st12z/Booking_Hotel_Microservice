package com.thuc.users.controller;

import com.thuc.users.constant.RoleConstant;
import com.thuc.users.dto.responseDto.RoleDto;
import com.thuc.users.dto.responseDto.SuccessResponseDto;
import com.thuc.users.service.IRoleService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleControllers {
    private final Logger log = LoggerFactory.getLogger(RoleControllers.class);
    private final IRoleService roleService;
    @GetMapping("")
    public ResponseEntity<SuccessResponseDto<List<RoleDto>>> getAllRoles(){
        log.debug("get all roles");
        SuccessResponseDto<List<RoleDto>> response = SuccessResponseDto.<List<RoleDto>>builder()
                .code(RoleConstant.STATUS_200)
                .message(RoleConstant.MESSAGE_200)
                .data(roleService.getAllRoles())
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

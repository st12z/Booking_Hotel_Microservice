package com.thuc.users.controller;

import com.thuc.users.constant.RoleConstant;
import com.thuc.users.dto.requestDto.RoleRequestDto;
import com.thuc.users.dto.responseDto.PageResponseDto;
import com.thuc.users.dto.responseDto.RoleDto;
import com.thuc.users.dto.responseDto.SuccessResponseDto;
import com.thuc.users.service.IRoleService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("roles-admin")
    public ResponseEntity<SuccessResponseDto<List<RoleDto>>> getAllRolesAdmin(){
        log.debug("get all roles");
        SuccessResponseDto<List<RoleDto>> response = SuccessResponseDto.<List<RoleDto>>builder()
                .code(RoleConstant.STATUS_200)
                .message(RoleConstant.MESSAGE_200)
                .data(roleService.getAllRolesAdmin())
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("all")
    public ResponseEntity<SuccessResponseDto<PageResponseDto<List<RoleDto>>>> getAllRoles(
            @RequestParam(defaultValue = "1") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize
    ){
        log.debug("get all roles");
        SuccessResponseDto<PageResponseDto<List<RoleDto>>> response = SuccessResponseDto.<PageResponseDto<List<RoleDto>>>builder()
                .code(RoleConstant.STATUS_200)
                .message(RoleConstant.MESSAGE_200)
                .data(roleService.getAllRolesByPage(pageNo,pageSize))
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PostMapping("create")
    public ResponseEntity<SuccessResponseDto<RoleDto>> createRole(@RequestBody RoleRequestDto roleDto){
        log.debug("create role");
        SuccessResponseDto<RoleDto> response = SuccessResponseDto.<RoleDto>builder()
                .code(RoleConstant.STATUS_200)
                .message(RoleConstant.MESSAGE_200)
                .data(roleService.createRole(roleDto))
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}

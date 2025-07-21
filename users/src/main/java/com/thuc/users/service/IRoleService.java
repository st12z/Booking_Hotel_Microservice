package com.thuc.users.service;

import com.thuc.users.dto.requestDto.RoleRequestDto;
import com.thuc.users.dto.responseDto.PageResponseDto;
import com.thuc.users.dto.responseDto.RoleDto;

import java.util.List;

public interface IRoleService {
    List<RoleDto> getAllRoles();

    PageResponseDto<List<RoleDto>> getAllRolesByPage(Integer pageNo, Integer pageSize);

    RoleDto createRole(RoleRequestDto roleDto);

    List<RoleDto> getAllRolesAdmin();
}

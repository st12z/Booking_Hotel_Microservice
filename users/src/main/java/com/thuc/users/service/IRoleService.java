package com.thuc.users.service;

import com.thuc.users.dto.responseDto.RoleDto;

import java.util.List;

public interface IRoleService {
    List<RoleDto> getAllRoles();
}

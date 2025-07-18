package com.thuc.users.converter;

import com.thuc.users.dto.responseDto.RoleDto;
import com.thuc.users.entity.RoleEntity;


public class RoleConverter {
    public static RoleDto toRoleDto(RoleEntity role) {
        return RoleDto.builder()
                .id(role.getId())
                .name(role.getName())
                .build();
    }
}

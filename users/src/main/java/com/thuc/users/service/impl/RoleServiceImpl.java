package com.thuc.users.service.impl;

import com.thuc.users.converter.RoleConverter;
import com.thuc.users.dto.responseDto.RoleDto;
import com.thuc.users.entity.RoleEntity;
import com.thuc.users.repository.RoleRepository;
import com.thuc.users.service.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements IRoleService {
    private final RoleRepository roleRepository;
    @Override
    public List<RoleDto> getAllRoles() {
        List<RoleEntity> roles = roleRepository.findAll();
        return roles.stream().map(RoleConverter::toRoleDto).toList();
    }
}

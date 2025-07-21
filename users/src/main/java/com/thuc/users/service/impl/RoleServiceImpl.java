package com.thuc.users.service.impl;

import com.thuc.users.converter.RoleConverter;
import com.thuc.users.dto.requestDto.RoleRequestDto;
import com.thuc.users.dto.responseDto.PageResponseDto;
import com.thuc.users.dto.responseDto.RoleDto;
import com.thuc.users.entity.PermissionEntity;
import com.thuc.users.entity.RoleEntity;
import com.thuc.users.exception.ResourceAlreadyException;
import com.thuc.users.repository.PermissionRepository;
import com.thuc.users.repository.RoleRepository;
import com.thuc.users.service.IKeycloakAccountService;
import com.thuc.users.service.IRoleService;
import com.thuc.users.utils.RoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements IRoleService {
    private final RoleRepository roleRepository;
    private final IKeycloakAccountService keycloakAccountService;
    private final PermissionRepository permissionRepository;

    @Override
    public List<RoleDto> getAllRoles() {
        List<RoleEntity> roles = roleRepository.findAll();
        return roles.stream().map(RoleConverter::toRoleDto).toList();
    }

    @Override
    public PageResponseDto<List<RoleDto>> getAllRolesByPage(Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by(Sort.Direction.ASC,"id"));
        Page<RoleEntity> roles = roleRepository.findAll(pageable);
        return PageResponseDto.<List<RoleDto>>builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .total(roles.getTotalElements())
                .dataPage(roles.stream().map(RoleConverter::toRoleDto).toList())
                .build();
    }

    @Override
    public RoleDto createRole(RoleRequestDto roleDto) {
        RoleEntity exist = roleRepository.findByName(roleDto.getName());
        if(exist != null) {
            throw new ResourceAlreadyException("Role","name",roleDto.getName());
        }
        List<PermissionEntity> permissionEntities= permissionRepository.findAll();
        RoleEntity role = RoleEntity.builder()
                .name(roleDto.getName())
                .permissions(permissionEntities)
                .build();
        RoleEntity savedRole = roleRepository.save(role);
        keycloakAccountService.createRole(role.getName());
        return RoleConverter.toRoleDto(savedRole);
    }

    @Override
    public List<RoleDto> getAllRolesAdmin() {
        List<RoleEntity> roles = roleRepository.findByNotName(RoleEnum.USER.getValue());
        return roles.stream().map(RoleConverter::toRoleDto).toList();
    }

}

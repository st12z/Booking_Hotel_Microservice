package com.thuc.users.service;

import com.thuc.users.dto.requestDto.UserRequestDto;
import com.thuc.users.entity.RoleEntity;
import com.thuc.users.entity.UserEntity;
import org.apache.catalina.User;

import java.util.List;

public interface IKeycloakAccountService {
    public boolean createUser(UserRequestDto user) ;

    void createRole(String name);

    boolean updateRoleByUser(UserEntity existUser, List<Integer> roleIds);

    boolean resetPassword(UserEntity user, String passwordRaw);

    boolean createStaff(UserRequestDto user,List<RoleEntity> roles );
}

package com.thuc.users.service;

import com.thuc.users.dto.requestDto.UserRequestDto;
import com.thuc.users.entity.UserEntity;

public interface IKeycloakService {
    public void createUser(UserEntity user) ;
}

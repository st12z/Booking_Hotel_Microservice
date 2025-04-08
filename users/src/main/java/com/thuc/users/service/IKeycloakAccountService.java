package com.thuc.users.service;

import com.thuc.users.dto.requestDto.UserRequestDto;

public interface IKeycloakAccountService {
    public void createUser(UserRequestDto user) ;

}

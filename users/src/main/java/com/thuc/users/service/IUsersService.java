package com.thuc.users.service;

import com.thuc.users.dto.requestDto.UserRequestDto;
import com.thuc.users.dto.responseDto.UserDto;

public interface IUsersService {

    UserDto createUser(UserRequestDto user);
}

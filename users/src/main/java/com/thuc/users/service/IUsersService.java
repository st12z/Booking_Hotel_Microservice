package com.thuc.users.service;

import com.thuc.users.dto.requestDto.RoomChatsDto;
import com.thuc.users.dto.requestDto.UserRequestDto;
import com.thuc.users.dto.responseDto.UserDto;

import java.util.Map;

public interface IUsersService {

    UserDto createUser(UserRequestDto user);

    Map<String,String> getToken(String code);

    Map<String,String> getAccessTokenByRefresh();

    void logout();

    UserDto getUserByEmail(String email);

    UserDto getInfoUserById(Integer id);

    RoomChatsDto createRoomChats(RoomChatsDto roomChats);
}

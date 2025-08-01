package com.thuc.users.service;

import com.thuc.users.dto.requestDto.FilterUserDto;
import com.thuc.users.dto.requestDto.RoomChatRequestDto;
import com.thuc.users.dto.requestDto.UserRequestDto;
import com.thuc.users.dto.responseDto.PageResponseDto;
import com.thuc.users.dto.responseDto.StatisticVisitByMonth;
import com.thuc.users.dto.responseDto.UserDto;
import jakarta.validation.Valid;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface IUsersService {

    UserDto createUser(UserRequestDto user);

    Map<String,String> getToken(String code);

    Map<String,String> getAccessTokenByRefresh();

    void logout();

    UserDto getUserByEmail(String email);

    UserDto getInfoUserById(Integer id);

    RoomChatRequestDto createRoomChats(RoomChatRequestDto roomChats);

    int updateUserVisits(Integer userId);

    Integer getAmountVisits();

    Integer getAmountUsers();

    List<StatisticVisitByMonth> getAmountVisitsByMonth(Integer month);

    List<UserDto> getAllUsersAdmin();

    PageResponseDto<List<UserDto>> getAllUsersByPage(FilterUserDto filterDto) throws ParseException;

    PageResponseDto<List<UserDto>> getSearchUsers(String keyword, Integer pageNo, Integer pageSize);

    UserDto updateRolesByUser(Integer id, List<Integer> roleIds);

    UserDto resetPassword(Integer id);

    UserDto createStaff(@Valid UserRequestDto user);
}

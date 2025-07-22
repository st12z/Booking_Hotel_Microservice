package com.thuc.users.converter;

import com.thuc.users.dto.requestDto.UserRequestDto;
import com.thuc.users.dto.responseDto.UserDto;
import com.thuc.users.entity.RoleEntity;
import com.thuc.users.entity.UserEntity;

public class UsersConverter {
    public static  UserEntity toUserEntity(UserRequestDto userRequestDto) {
            return UserEntity.builder()
                .email(userRequestDto.getEmail())
                .firstName(userRequestDto.getFirstName())
                .lastName(userRequestDto.getLastName())
                .password(userRequestDto.getPassword())
                .address(userRequestDto.getAddress())
                .city(userRequestDto.getCity())
                .district(userRequestDto.getDistrict())
                .gender(userRequestDto.getGender())
                .village(userRequestDto.getVillage())
                .phoneNumber(userRequestDto.getPhoneNumber())
                .birthday(userRequestDto.getBirthday())
                .build();
    }
    public static UserDto toUserDto(UserEntity userEntity) {
        return UserDto.builder()
                .id(userEntity.getId())
                .email(userEntity.getEmail())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .avatar(userEntity.getAvatar())
                .address(userEntity.getAddress())
                .city(userEntity.getCity())
                .district(userEntity.getDistrict())
                .gender(userEntity.getGender())
                .village(userEntity.getVillage())
                .phoneNumber(userEntity.getPhoneNumber())
                .birthday(userEntity.getBirthday())
                .roles(userEntity.getRoles().stream().map(RoleEntity::getName).toList())
                .roleDtos(userEntity.getRoles().stream().map(RoleConverter::toRoleDto).toList())
                .createdAt(userEntity.getCreatedAt())
                .build();
    }
}

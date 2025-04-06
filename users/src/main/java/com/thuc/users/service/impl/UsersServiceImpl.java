package com.thuc.users.service.impl;

import com.thuc.users.converter.UsersConverter;
import com.thuc.users.dto.requestDto.UserRequestDto;
import com.thuc.users.dto.responseDto.UserDto;
import com.thuc.users.entity.UserEntity;
import com.thuc.users.exception.ResourceAlreadyException;
import com.thuc.users.repository.UserRepository;
import com.thuc.users.service.IUsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements IUsersService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public UserDto createUser(UserRequestDto user) {
        UserEntity userEntity = userRepository.findByEmail(user.getEmail());
        if(userEntity != null) {
            throw new ResourceAlreadyException("User already exists");
        }
        userEntity = UsersConverter.toUserEntity(user);
        userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
        UserEntity saveUser= userRepository.save(userEntity);
        return UsersConverter.toUserDto(saveUser);
    }
}

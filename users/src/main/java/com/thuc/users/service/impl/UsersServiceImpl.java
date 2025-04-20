package com.thuc.users.service.impl;

import com.thuc.users.converter.UsersConverter;
import com.thuc.users.dto.requestDto.UserRequestDto;
import com.thuc.users.dto.responseDto.UserDto;
import com.thuc.users.entity.RoleEntity;
import com.thuc.users.entity.UserEntity;
import com.thuc.users.exception.ResourceAlreadyException;
import com.thuc.users.exception.ResourceNotFoundException;
import com.thuc.users.repository.RoleRepository;
import com.thuc.users.repository.UserRepository;
import com.thuc.users.service.IKeycloakAccountService;
import com.thuc.users.service.IKeycloakService;
import com.thuc.users.service.IUsersService;
import com.thuc.users.utils.RoleEnum;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements IUsersService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final IKeycloakAccountService keycloakAccountService;
    private final IKeycloakService keycloakService;
    private final RoleRepository roleRepository;
    private final Logger logger = LoggerFactory.getLogger(UsersServiceImpl.class);
    private final StreamBridge streamBridge;
    @Override
    public UserDto createUser(UserRequestDto user) {
        logger.debug("Creating a new user");
        UserEntity userEntity = userRepository.findByEmail(user.getEmail());
        if(userEntity != null) {
            throw new ResourceAlreadyException("User already exists");
        }
        RoleEntity roleUser = roleRepository.findByName(RoleEnum.USER.getValue());
        userEntity = UsersConverter.toUserEntity(user);
        userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
        userEntity.setRoles(List.of(roleUser));
        UserEntity saveUser= userRepository.save(userEntity);
        keycloakAccountService.createUser(user);
        sendCommunication(user);
        return UsersConverter.toUserDto(saveUser);
    }

    private void sendCommunication(UserRequestDto user) {
        logger.debug("Sending communication with {}",user);
        var result = streamBridge.send("sendCommunication-out-0",user);
        logger.debug("Received communication with {}",result);
    }

    @Override
    public Map<String,String> getToken(String code) {
        return keycloakService.getToken(code);
    }

    @Override
    public Map<String, String> getAccessTokenByRefresh() {
        return keycloakService.getAccessTokenByRefresh();
    }

    @Override
    public void logout() {
        keycloakService.logout();
    }

    @Override
    public UserDto getUserByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        if(userEntity == null) {
            throw new ResourceNotFoundException("User", email);
        }
        return UsersConverter.toUserDto(userEntity);
    }
}

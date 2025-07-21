package com.thuc.users.service.impl;


import com.thuc.users.dto.requestDto.UserRequestDto;
import com.thuc.users.entity.PermissionEntity;
import com.thuc.users.entity.RoleEntity;
import com.thuc.users.entity.UserEntity;
import com.thuc.users.exception.ResourceNotFoundException;
import com.thuc.users.repository.RoleRepository;
import com.thuc.users.repository.UserRepository;
import com.thuc.users.service.IKeycloakAccountService;
import com.thuc.users.utils.RoleEnum;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KeycloakAccountServiceImpl implements IKeycloakAccountService {
    private final Logger logger = LoggerFactory.getLogger(KeycloakAccountServiceImpl.class);
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    @Value("${keycloak-credential.auth-server-url}")
    private String authServerUrl;

    @Value("${keycloak-credential.realm}")
    private String realm;

    @Value("${keycloak-credential.client-id}")
    private String clientId;

    @Value("${keycloak-credential.client-secret}")
    private String clientSecret;

    private Keycloak getKeycloakInstance() {
        return KeycloakBuilder.builder()
                .serverUrl(authServerUrl)
                .realm(realm)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .build();
    }
    public boolean createUser(UserRequestDto user) {
        try {
            logger.debug("Creating user in Keycloak: " + user.getEmail());
            Keycloak keycloak = getKeycloakInstance();

            // Tạo user mới
            UserRepresentation userRepresentation = new UserRepresentation();
            userRepresentation.setFirstName(user.getFirstName());
            userRepresentation.setLastName(user.getLastName());
            userRepresentation.setEmail(user.getEmail());
            userRepresentation.setEnabled(true);
            userRepresentation.setEmailVerified(true);
            userRepresentation.setUsername(user.getEmail());

            // Thiết lập mật khẩu
            CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
            credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
            credentialRepresentation.setValue(user.getPassword());
            credentialRepresentation.setTemporary(false);
            userRepresentation.setCredentials(Collections.singletonList(credentialRepresentation));

            // Gửi yêu cầu tạo user
            UsersResource usersResource = keycloak.realm(realm).users();
            var response = usersResource.create(userRepresentation);
            logger.debug("User created successfully with {}",response);
            // Kiểm tra phản hồi từ Keycloak
            if (response.getStatus() == 201) {
                logger.debug("User created successfully");

                String keycloakId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
                logger.debug("User created with id: " + keycloakId);
                RoleEntity roleUser = roleRepository.findByName(RoleEnum.USER.getValue());
                List<String> roleNames = List.of(roleUser.getName());
                assignRoleToUser(keycloak, keycloakId,roleNames);
                return true;
            } else {
                logger.error("User creation failed with status: " + response.getStatus());
                logger.error("Response body: " + response.readEntity(String.class));
                return false;
            }
        } catch (Exception e) {
            logger.error("Error creating user in Keycloak", e);
            throw new RuntimeException("Error creating user in Keycloak: " + e.getMessage());
        }
    }
    @Override
    public boolean createStaff(UserRequestDto user,List<RoleEntity> roles ) {
        try {
            logger.debug("Creating user in Keycloak: " + user.getEmail());
            Keycloak keycloak = getKeycloakInstance();

            // Tạo user mới
            UserRepresentation userRepresentation = new UserRepresentation();
            userRepresentation.setFirstName(user.getFirstName());
            userRepresentation.setLastName(user.getLastName());
            userRepresentation.setEmail(user.getEmail());
            userRepresentation.setEnabled(true);
            userRepresentation.setEmailVerified(true);
            userRepresentation.setUsername(user.getEmail());

            // Thiết lập mật khẩu
            CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
            credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
            credentialRepresentation.setValue(user.getPassword());
            credentialRepresentation.setTemporary(false);
            userRepresentation.setCredentials(Collections.singletonList(credentialRepresentation));

            // Gửi yêu cầu tạo user
            UsersResource usersResource = keycloak.realm(realm).users();
            var response = usersResource.create(userRepresentation);
            logger.debug("User created successfully with {}",response);
            // Kiểm tra phản hồi từ Keycloak
            if (response.getStatus() == 201) {
                logger.debug("User created successfully");

                String keycloakId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
                logger.debug("User created with id: " + keycloakId);
                List<String> roleNames = roles.stream().map(RoleEntity::getName).toList();
                assignRoleToUser(keycloak, keycloakId,roleNames);
                return true;
            } else {
                logger.error("User creation failed with status: " + response.getStatus());
                logger.error("Response body: " + response.readEntity(String.class));
                return false;
            }
        } catch (Exception e) {
            logger.error("Error creating user in Keycloak", e);
            throw new RuntimeException("Error creating user in Keycloak: " + e.getMessage());
        }
    }
    @Override
    public void createRole(String name) {
        try{
            Keycloak keycloak = getKeycloakInstance();
            List<String> roleNames = List.of(name);
            createRoleIfNotExists(keycloak, roleNames);
        }catch (Exception e){
            throw new RuntimeException("Error create role in Keycloak: " + e.getMessage());
        }
    }
    private String getKeycloakUserId(String email){
        Keycloak keycloak = getKeycloakInstance();
        List<UserRepresentation> users = keycloak.realm(realm)
                .users()
                .searchByUsername(email, true);
        if(!users.isEmpty()){
            UserRepresentation userRepresentation = users.get(0);
            return userRepresentation.getId();
        }
        return null;
    }
    @Override
    public boolean updateRoleByUser(UserEntity existUser, List<Integer> roleIds) {
        try{
            Keycloak keycloak = getKeycloakInstance();
            List<String> roleNames = roleIds.stream().map(id->{
                RoleEntity roleEntity = roleRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Role","id",String.valueOf(id)));
                return roleEntity.getName();
            }).toList();
            String keycloakId = getKeycloakUserId(existUser.getEmail());
            UserResource userResource = keycloak.realm(realm).users().get(keycloakId);
            List<RoleRepresentation> currentRoles = userResource.roles().realmLevel().listAll();
            if(!currentRoles.isEmpty()) {
                userResource.roles().realmLevel().remove(currentRoles);
            }
            assignRoleToUser(keycloak, keycloakId, roleNames);
            return true;
        }catch (Exception e){
            throw new RuntimeException("Error update role in Keycloak: " + e.getMessage());
        }
    }

    @Override
    public boolean resetPassword(UserEntity user, String passwordRaw) {
        try {
            logger.debug("reset password user in Keycloak: " + user.getEmail());
            Keycloak keycloak = getKeycloakInstance();
            String keycloakId = getKeycloakUserId(user.getEmail());
            CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
            credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
            credentialRepresentation.setValue(passwordRaw);
            credentialRepresentation.setTemporary(false);
            keycloak.realm(realm)
                    .users()
                    .get(keycloakId)
                    .resetPassword(credentialRepresentation);
            return true;

        } catch (Exception e) {
            logger.error("Error reset password user in Keycloak", e);
            throw new RuntimeException("Error reset password user in Keycloak: " + e.getMessage());
        }
    }




    // Gán role
    private void assignRoleToUser(Keycloak keycloak, String keycloakId,List<String> roles) {
        try{
            List<RoleRepresentation> role= createRoleIfNotExists(keycloak, roles);
            UserResource userResource = keycloak.realm(realm).users().get(keycloakId);
            logger.debug("Assigning user to Keycloak: " + userResource);
            userResource.roles().realmLevel().add(role);
        }catch (Exception e){
            throw new RuntimeException("Add role in Keycloak: " + e.getMessage());
        }

    }

    // Tạo List<Role> mới nếu không tồn tại
    private List<RoleRepresentation> createRoleIfNotExists(Keycloak keycloak, List<String> roles) {
        RolesResource rolesResource = keycloak.realm(realm).roles();
        List<RoleRepresentation> result = new ArrayList<>();
        try{
            for(String role : roles){
                RoleRepresentation roleRepresentation;
                try{
                    roleRepresentation = rolesResource.get(role).toRepresentation();
                }catch (Exception e){
                    roleRepresentation = new RoleRepresentation();
                    roleRepresentation.setName(role);
                    rolesResource.create(roleRepresentation);
                    roleRepresentation = rolesResource.get(role).toRepresentation();
                }
                result.add(roleRepresentation);
            }
            return result;
        }catch (Exception e){
            throw new RuntimeException("Error creating role in Keycloak: " + e.getMessage());
        }
    }

}


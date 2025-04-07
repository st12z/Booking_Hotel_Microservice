package com.thuc.users.service.impl;


import com.thuc.users.entity.PermissionEntity;
import com.thuc.users.entity.RoleEntity;
import com.thuc.users.entity.UserEntity;
import com.thuc.users.service.IKeycloakService;
import lombok.RequiredArgsConstructor;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RoleResource;
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
public class KeycloakServiceImpl implements IKeycloakService {
    private final Logger logger = LoggerFactory.getLogger(KeycloakServiceImpl.class);
    @Value("${keycloak.auth-server-url}")
    private String authServerUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.client-secret}")
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
    public void createUser(UserEntity user) {
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

                String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
                logger.debug("User created with id: " + userId);
                List<String> roles =user.getRoles().stream().map(RoleEntity::getName).collect(Collectors.toList());
                roles.addAll(user.getRoles().stream().flatMap(role->role.getPermissions().stream()).map(PermissionEntity::getName).toList());
                assignRoleToUser(keycloak, userId,roles);

            } else {
                logger.error("User creation failed with status: " + response.getStatus());
                logger.error("Response body: " + response.readEntity(String.class));
            }
        } catch (Exception e) {
            logger.error("Error creating user in Keycloak", e);
            throw new RuntimeException("Error creating user in Keycloak: " + e.getMessage());
        }
    }

    private void assignRoleToUser(Keycloak keycloak, String userId,List<String> roles) {
        try{
            List<RoleRepresentation> role= createRoleIfNotExists(keycloak, roles);
            UserResource userResource = keycloak.realm(realm).users().get(userId);
            logger.debug("Assigning user to Keycloak: " + userResource);
            userResource.roles().realmLevel().add(role);
        }catch (Exception e){
            throw new RuntimeException("Add role in Keycloak: " + e.getMessage());
        }

    }

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


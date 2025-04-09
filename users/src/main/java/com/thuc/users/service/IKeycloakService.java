package com.thuc.users.service;

import java.util.Map;

public interface IKeycloakService {
    public Map<String,String> getToken(String code);

    Map<String, String> getAccessTokenByRefresh();

    void logout();
}

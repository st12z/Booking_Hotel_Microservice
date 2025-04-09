package com.thuc.users.service.impl;

import com.thuc.users.service.IKeycloakService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class KeycloakServiceImpl implements IKeycloakService {
    private final Logger log = LoggerFactory.getLogger(KeycloakServiceImpl.class);
    @Value("${keycloak-authorization.token-endpoint}")
    private String tokenEndpoint;

    @Value("${keycloak-authorization.logout-endpoint}")
    private String logoutEndpoint;

    @Value("${keycloak-authorization.client-id}")
    private String clientId;

    @Value("${keycloak-authorization.client-secret}")
    private String clientSecret;

    @Value("${keycloak-authorization.redirect-uri}")
    private String redirectUri;

    @Value("${keycloak-authorization.grant-type}")
    private String grantType;
    @Override
    public Map<String, String> getToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();
        form.add("client_id", clientId);
        form.add("client_secret", clientSecret);
        form.add("redirect_uri", redirectUri);
        form.add("code", code);
        form.add("grant_type", grantType);
        log.debug("form :{}",form.toString());
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(form, headers);
        RestTemplate restTemplate = new RestTemplate();
        try{
            ResponseEntity<Map> response = restTemplate.postForEntity(tokenEndpoint, request, Map.class);
            if(response.getStatusCode().is2xxSuccessful()) {
                if(response.getBody().containsKey("access_token") && response.getBody().containsKey("refresh_token")) {
                    // Trả access token về phía clien
                    Map<String,String> result = new HashMap<>();
                    result.put("access_token", response.getBody().get("access_token").toString());
                    result.put("expires_in", response.getBody().get("expires_in").toString());
                    result.put("refresh_token", response.getBody().get("refresh_token").toString());
                    // Cấu hình HttpOnly cookie
                    return result;
                }
                else{
                    throw new RuntimeException("Access Token and Refresh Token not in response");
                }
            }
            else{
                throw new RuntimeException("Get access token failed");
            }
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }

    }

    @Override
    public Map<String, String> getAccessTokenByRefresh() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Cookie[] cookies = request.getCookies();
        log.debug("cookies :{}",cookies);
        String refreshToken = getRefreshToken();
        if(refreshToken == null) {
            throw new RuntimeException("Refresh token not found");
        }
        log.debug("refreshToken :{}",refreshToken);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();
        form.add("client_id", clientId);
        form.add("client_secret", clientSecret);
        form.add("grant_type", "refresh_token");
        form.add("refresh_token", refreshToken);
        log.debug("form :{}", form);
        HttpEntity<MultiValueMap<String,Object>>  httpEntity = new HttpEntity<>(form,headers);
        RestTemplate restTemplate = new RestTemplate();
        try{
            ResponseEntity<Map> response = restTemplate.postForEntity(tokenEndpoint, httpEntity, Map.class);
            if(response.getStatusCode().is2xxSuccessful()) {
                Map<String,String> result = new HashMap<>();
                if(response.getBody().containsKey("access_token")) {
                    result.put("access_token", response.getBody().get("access_token").toString());
                    return result;
                }
                else{
                    throw new RuntimeException("Access Token not in response");
                }
            }
            else{
                throw new RuntimeException("Get access token failed");
            }
        }catch (Exception e){
            throw new RuntimeException("Refresh token not valid");
        }
    }

    @Override
    public void logout() {
        String refreshToken = getRefreshToken();
        try{
            if(refreshToken != null) {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
                params.add("client_id", clientId);
                params.add("client_secret", clientSecret);
                params.add("refresh_token", refreshToken);

                HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);
                RestTemplate restTemplate = new RestTemplate();

                restTemplate.postForEntity(logoutEndpoint, entity, String.class);
            }
        }catch (Exception e){

        }
    }
    private String getRefreshToken() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Cookie[] cookies = request.getCookies();
        log.debug("cookies :{}",cookies);
        String refreshToken = null;
        if(cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if(cookie.getName().equals("refresh_token")) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }
        return refreshToken;
    }
}

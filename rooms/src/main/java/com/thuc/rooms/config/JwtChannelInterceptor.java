package com.thuc.rooms.config;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Component
public class JwtChannelInterceptor implements ChannelInterceptor {
    private final JwtDecoder jwtDecoder;
    public JwtChannelInterceptor(JwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
    }
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authHeader = accessor.getFirstNativeHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring("Bearer ".length());
                try{
                    Jwt jwt = jwtDecoder.decode(token);
                    String email = jwt.getClaimAsString("preferred_username");
                    Map<String,Object> realmAccess =(Map<String, Object>) jwt.getClaims().get("realm_access");
                    Collection<GrantedAuthority> authorities = ((List<String>) realmAccess.get("roles")).stream()
                            .map(role -> "ROLE_"+role)
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());
                    Authentication authentication = new UsernamePasswordAuthenticationToken(email,null,authorities);
                    accessor.setUser(authentication);
                }catch (Exception e){
                    throw new AccessDeniedException("JWT không hợp lệ: " + e.getMessage());
                }
            }
            else{
                throw new AccessDeniedException("Không tìm thấy token JWT trong kết nối WebSocket");
            }
        }
        return ChannelInterceptor.super.preSend(message, channel);
    }
}

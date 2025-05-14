package com.thuc.gatewayserver.config;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AddUserInfoFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .cast(JwtAuthenticationToken.class)
                .flatMap(auth->{
                    Jwt jwt = auth.getToken();
                    String email = jwt.getClaim("preferred_username");
                    ServerHttpRequest request = exchange.getRequest().mutate()
                            .header("X-User-Email", email)
                            .build();
                    return chain.filter(exchange.mutate().request(request).build());
                }).switchIfEmpty(chain.filter(exchange));
    }
}
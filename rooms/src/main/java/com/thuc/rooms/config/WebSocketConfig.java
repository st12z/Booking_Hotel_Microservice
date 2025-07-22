package com.thuc.rooms.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Map;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final Logger logger = LoggerFactory.getLogger(WebSocketConfig.class);


    @Value("${URL_FRONTEND}")
    private String urlFrontEnd;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        try{
            logger.debug("register stomp endpoints  urlFrontEnd {}",urlFrontEnd);
            registry.addEndpoint("/ws")
                    .setAllowedOriginPatterns(urlFrontEnd)
                    .withSockJS()
                    .setInterceptors(new HttpSessionHandshakeInterceptor() {
                        @Override
                        public boolean beforeHandshake(ServerHttpRequest request,
                                                       ServerHttpResponse response,
                                                       WebSocketHandler wsHandler,
                                                       Map<String, Object> attributes) throws Exception {
                            String origin = ((ServletServerHttpRequest) request)
                                    .getServletRequest().getHeader("Origin");
                            logger.debug("WebSocket handshake origin = {}", origin);
                            return super.beforeHandshake(request, response, wsHandler, attributes);
                        }
                    });
            ;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        try{
            registry.setApplicationDestinationPrefixes("/app");
            registry.enableSimpleBroker("/topic","/queue","/user");
            registry.setUserDestinationPrefix("/user");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}

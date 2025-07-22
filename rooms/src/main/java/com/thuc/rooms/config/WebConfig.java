package com.thuc.rooms.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final Logger log = LoggerFactory.getLogger(WebConfig.class);


    @Value("${URL_FRONTEND}")
    private String urlFrontEnd;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        try{
            log.debug("cors with  urlFrontEnd {}",urlFrontEnd);
            registry.addMapping("/ws/**")
                    .allowedOriginPatterns(urlFrontEnd)
                    .allowedMethods("*")
                    .allowCredentials(true);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
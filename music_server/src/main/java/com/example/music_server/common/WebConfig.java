package com.example.music_server.common;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {


    @Override
    public void addCorsMappings(CorsRegistry registry){
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:8080")
                .allowedMethods("GET", "POST", "PUT", "DELETE","OPTIONS","PATCH")
                .allowCredentials(true)
                .allowedHeaders("token", "Content-Type");
        // 添加调试信息
        System.out.println("CORS Config applied!");
    }
}

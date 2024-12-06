package com.example.music_server.common.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(WebConfig.class);
    @Override
    public void addCorsMappings(CorsRegistry registry){
        logger.info("开始设置跨域配置");
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:8080")
                .allowedMethods("GET", "POST", "PUT", "DELETE","OPTIONS","PATCH")
//                .allowedHeaders("Content-Type", "token", "Authorization", "multipart/form-data");
                .allowedHeaders("*")
                .allowCredentials(true);

        logger.info("允许的源: {}", "http://localhost:8080");
        logger.info("允许的方法: {}", "GET, POST, PUT, DELETE, OPTIONS, PATCH");
        logger.info("允许的请求头: {}", "Content-Type, token, Authorization");
        logger.info("是否允许携带凭证: {}", true);
        // 添加调试信息
        System.out.println("CORS Config applied!");
    }
}

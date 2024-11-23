package com.example.music_server.common;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Resource
    private JwtInterceptor jwtInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(jwtInterceptor).addPathPatterns("/**")
                .addPathPatterns("/") //拦截所有的请求路径
                .excludePathPatterns("/users/login")
                .excludePathPatterns("/admin/login")
                .excludePathPatterns("/singers")
                .excludePathPatterns("/singers/**")
                .excludePathPatterns("/songs")
                .excludePathPatterns("/songs/**")
                .excludePathPatterns("//img/**")
                .excludePathPatterns("//song/**");

    }

    @Bean
    public JwtInterceptor jwtInterceptor(){
        return  new JwtInterceptor();
    }
}

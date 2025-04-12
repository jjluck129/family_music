package com.example.music_server.common.config;

import com.example.music_server.common.JwtInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import javax.annotation.Resource;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(InterceptorConfig.class);

    @Resource
    private JwtInterceptor jwtInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        logger.info("开始注册拦截器");
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**") //拦截所有的请求路径
                .excludePathPatterns("/users/login")
                .excludePathPatterns("/users/register")
                .excludePathPatterns("/admin/login")
                .excludePathPatterns("/upload")
                .excludePathPatterns("/imserver/**")
                .excludePathPatterns("/singers")
                .excludePathPatterns("/singers/**")
                .excludePathPatterns("/songs")
                .excludePathPatterns("/songs/**")
                .excludePathPatterns("/songs/uploadMp3")
                .excludePathPatterns("//img/**")
                .excludePathPatterns("//song/**");
        logger.info("已注册拦截器，拦截路径: {}", "/**");
        logger.info("排除拦截的路径: {}", "/users/login, /admin/login, /upload, /singers, /singers/**, /songs, /songs/**, //img/**, //song/**");
    }

}

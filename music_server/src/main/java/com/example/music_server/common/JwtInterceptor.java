package com.example.music_server.common;

import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.music_server.entity.User;
import com.example.music_server.exception.ServiceException;
import com.example.music_server.mapper.UserMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

@Component
public class JwtInterceptor  implements HandlerInterceptor {

    @Resource
    private UserMapper userMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true; // 直接放行预检请求
        }
        System.out.println("Request URI: " + request.getRequestURI());

        String token = request.getHeader("token");
        System.out.println("接受到的token为："+token);
        if(StrUtil.isBlank(token)){
            token = request.getParameter("token");
        }
        //执行认证
        if(StrUtil.isBlank(token)){
            throw new ServiceException("401","请登录，并且提供有效的token");
        }
        // 获取 token 中的 userid
        String userId;
        try {
            userId = JWT.decode(token).getAudience().get(0);
        } catch (JWTDecodeException j){
            throw new ServiceException("401","请登录，无效的token格式");
        }

        //根据token中的userid查询数据库
        User user = userMapper.findById(Integer.valueOf(userId));
        if(user == null){
            throw new ServiceException("401","请登录，用户名不存再或者已删除");
        }
        // 验证 token 是否过期
        if (JWT.decode(token).getExpiresAt().before(new Date())) {
            throw new ServiceException("401", "token 已过期");
        }
        // 通过用户名密码加密生成一个验证器 token
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getPassword())).build();
        try {
            jwtVerifier.verify(token); // 验证token
        } catch (JWTVerificationException e){
            throw new ServiceException("401","请登录");
        }
        return true;
    }
}

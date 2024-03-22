package cn.tockey.interceptors;

import cn.tockey.config.JwtProperties;
import cn.tockey.domain.User;
import cn.tockey.utils.JwtUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

// 登录拦截器
@Component // 注入到spring容器
public class LoginInterceptors implements HandlerInterceptor {
    @Resource
    private JwtProperties jwtProperties;

    //@Override
    //public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    //    //return HandlerInterceptor.super.preHandle(request, response, handler);
    //    // 从请求头中获取token
    //    String token = request.getHeader("Authorization");
    //    // 解析token
    //    try {
    //        JwtUtils.getObjectFromToken(token, jwtProperties.getPublicKey(), Object.class);
    //        // 如果解析成功，返回true，放行
    //        return true;
    //    } catch (Exception e) {
    //        e.printStackTrace();
    //        // 如果解析失败，返回false，拦截
    //        response.setStatus(401);
    //        response.setContentType("application/json;charset=utf-8");
    //        response.getWriter().write("无效的 token");
    //        return false;
    //    }
    //}
}

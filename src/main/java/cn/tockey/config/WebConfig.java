package cn.tockey.config;

import cn.tockey.interceptors.LoginInterceptors;
import jakarta.annotation.Resource;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
 
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Resource
    private LoginInterceptors loginInterceptors;

    // 拦截器配置
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //WebMvcConfigurer.super.addInterceptors(registry);
        registry.addInterceptor(loginInterceptors)
                //.addPathPatterns("/**") // 拦截所有路径
                .excludePathPatterns(
                        "/user/login", // 放行登录和注册接口
                        "/user/registry",
                        "/user/checkUsername/**", // 放行检查用户名
                        "/swagger-ui/**", // 放行swagger
                        "/doc/**",
                        "/v3/api-docs/**"
                );
    }


    // 跨域配置
    //@Override
    //public void addCorsMappings(CorsRegistry registry) {
    //    registry.addMapping("/**") // 对所有路径应用跨域配置
    //            .allowedOrigins("*") // 允许特定来源
    //            .allowedMethods("GET", "POST", "PUT", "DELETE"); // 允许的方法
    //}
    //跨域设置是在后端工程里使用跨域拦截器进行配置的，而添加的自定义登录拦截器是在跨域拦截器之前执行的，导致跨域拦截器失效
    //解决方法：让跨域配置在登录拦截器之前执行。而Filter的执行顺序大于自定义拦截器，所以改为在Filter里实现跨域的配置。
}
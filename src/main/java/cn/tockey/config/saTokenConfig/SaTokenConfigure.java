package cn.tockey.config.saTokenConfig;


import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// 路由拦截鉴权配置
@Configuration
public class SaTokenConfigure implements WebMvcConfigurer {
    // 注册拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SaInterceptor(handle -> {
            // 对所有路由进行登录鉴权
            SaRouter
                    .match("/**") // 匹配所有路由
                    .notMatch(
                            "/user/login", // 放行登录接口
                            "/user/register", // 放行注册接口
                            // 放行 swagger
                            "/doc",
                            "/swagger-ui/**",
                            "/v3/api-docs/**",
                            "/websocket/**", // 放行 websocket
                            "/oauth2/**" // 放行 oauth2
                    ).check(r -> StpUtil.checkLogin()); // 登录校验

            // 对 user 模块下的所有接口进行角色鉴权 (只允许 admin 角色访问)
            //SaRouter.match("/user/**", r -> StpUtil.checkRole("admin"));
        })).addPathPatterns("/**"); // 拦截所有路由
    }
}

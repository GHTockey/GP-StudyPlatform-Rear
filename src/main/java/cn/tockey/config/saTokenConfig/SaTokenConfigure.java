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
                    .notMatch( // 但不匹配以下路由
                            "/user/login", // 放行登录接口
                            "/user/register", // 放行注册接口
                            // 放行 swagger
                            "/doc",
                            "/swagger-ui/**",
                            "/v3/api-docs/**",
                            "/websocket/**", // 放行 websocket
                            "/user/token/**", // 放行 通过token获取用户数据API
                            "/other/login/**", // 放行 获取第三方登录链接API
                            "/other/oauth/**", // 放行 通过 oKey 获取授权用户信息
                            "/user/oauth/**", // 放行 使用第三方账号注册本平台、绑定本平台用户
                            "/other/mail/send", // 放行 邮件发送API
                            "/website/**", // 放行 获取网站信息API
                            "/oauth/**" // 放行 oauth2
                    ).check(r -> StpUtil.checkLogin()); // 登录校验

            // 对 user 模块下的所有接口进行角色鉴权 (只允许 admin 角色访问)
            //SaRouter.match("/user/**", r -> StpUtil.checkRole("admin"));
            // 权限模块鉴权
            SaRouter.match("/permission/**", r -> StpUtil.checkRole("admin")); // 只有拥有 admin 角色的用户才能访问
            SaRouter.match("/permission/**", r -> StpUtil.checkPermission("/perm")); // 只有拥有 permission 权限的用户才能访问
        })).addPathPatterns("/**"); // 拦截所有路由
    }
}

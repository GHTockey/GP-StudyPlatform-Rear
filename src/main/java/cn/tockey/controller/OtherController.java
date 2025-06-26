package cn.tockey.controller;

import cn.tockey.config.TceRedisConfig;
import cn.tockey.config.oauth2.GitHubOAuth2Config;
import cn.tockey.config.oauth2.GiteeOAuth2Config;
import cn.tockey.domain.GiteeUser;
import cn.tockey.domain.GithubUser;
import cn.tockey.service.OtherService;
import cn.tockey.vo.BaseResult;
import cn.tockey.vo.EmailCodeVo;
import cn.tockey.vo.MyResult;
import com.alibaba.fastjson2.JSON;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/other")
public class OtherController {
    @Resource
    private OtherService otherService;
    @Resource
    private GiteeOAuth2Config giteeOAuth2Config;
    @Resource
    private GitHubOAuth2Config gitHubOAuth2Config;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Operation(summary = "上传图片")
    @PostMapping("/image/upload")
    BaseResult<String> uploadImage( MultipartFile file) {
        try {
            String updRes = otherService.uploadImage(file);
            return BaseResult.ok("上传成功", updRes);
        } catch (IOException e) {
            e.printStackTrace();
            return BaseResult.error("上传失败");
        }
    }

    // 获取 OAuth 用户信息 controller
    @Operation(summary = "获取 OAuth 用户信息")
    @GetMapping("/oauth/user")
    BaseResult<Object> getOAuthUserInfo(@RequestParam String type, @RequestParam String oKey){
        switch (type.toUpperCase()) {
            case "GITHUB":
                return BaseResult.ok("获取成功",otherService.getOAuthUserInfo(oKey, type, GithubUser.class));
            case "GITEE":
                return BaseResult.ok("获取成功", otherService.getOAuthUserInfo(oKey, type, GiteeUser.class));
            default: return BaseResult.error("获取失败");
        }
    }

    //根据 OAuth 类型跳转到不同的第三方登录页面
    @Operation(summary = "跳转到第三方登录页面")
    @GetMapping("/login/{type}")
    BaseResult<String> login(@PathVariable String type) {
        switch (type.toUpperCase()) {
            case "GITHUB":
                return BaseResult.ok("获取成功", gitHubOAuth2Config.getAuthorizeUrl());
            case "GITEE":
                return BaseResult.ok("获取成功", giteeOAuth2Config.getAuthorizeUrl());
            default: return BaseResult.error("获取失败");
        }
    }

    // 发送邮箱验证码
    @Operation(summary = "发送邮件")
    @PostMapping("/mail/send")
    MyResult<Date> sendSimpleMailMessage2(@RequestParam String to) {
        // 邮箱
        if (to.isEmpty()) {
            return MyResult.error("请输入邮箱");
        }

        // 先判断是否已经发送过验证码
        String strObj = stringRedisTemplate.opsForValue().get(TceRedisConfig.emailCodeKey + to);
        EmailCodeVo emailCodeVo = JSON.parseObject(strObj, EmailCodeVo.class);
        if (emailCodeVo != null) {
            return MyResult.ok("该邮箱短时间内已发送过验证码，请勿频繁发送", emailCodeVo.getExpireTime());
        }

        EmailCodeVo sendEmailCodeVoResult = otherService.sendEmailVerificationCode(to);
        if (sendEmailCodeVoResult == null) {
            return MyResult.error("发送失败");
        }

        return MyResult.ok("发送成功", sendEmailCodeVoResult.getExpireTime());
    }
}

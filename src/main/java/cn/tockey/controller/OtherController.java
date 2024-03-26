package cn.tockey.controller;

import cn.tockey.config.oauth2.GitHubOAuth2Config;
import cn.tockey.config.oauth2.GiteeOAuth2Config;
import cn.tockey.domain.GithubUser;
import cn.tockey.service.OtherService;
import cn.tockey.vo.BaseResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/other")
public class OtherController {
    @Resource
    private OtherService otherService;
    @Resource
    private GiteeOAuth2Config giteeOAuth2Config;
    @Resource
    private GitHubOAuth2Config gitHubOAuth2Config;

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

    @GetMapping("/oauth/user")
    BaseResult<Object> getOAuthUserInfo(@RequestParam String type, @RequestParam String oKey){
        switch (type.toUpperCase()) {
            case "GITHUB":
                return BaseResult.ok("获取成功",otherService.getOAuthUserInfo(oKey, type, GithubUser.class));
            case "GITEE":
                return BaseResult.ok("zzz");
            default: return BaseResult.error("获取失败");
        }
    }

    //根据 OAuth 类型跳转到不同的第三方登录页面
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
}

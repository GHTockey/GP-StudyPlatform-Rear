package cn.tockey.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // Controller + ResponseBody 的组合注解；前者用于定义控制器类，后者用于将方法返回值转换为 JSON 格式
@RequestMapping("/oauth2")
public class OAuth2LoginController {

    //@GetMapping("/clientId")

}

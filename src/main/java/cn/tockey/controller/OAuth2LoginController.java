package cn.tockey.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.tockey.config.RestTemplateConfig;
import cn.tockey.config.oauth2.GitHubOAuth2Config;
import cn.tockey.domain.GithubUser;
import cn.tockey.domain.User;
import cn.tockey.service.UserService;
import cn.tockey.vo.BaseResult;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.*;

import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

//@RestController // Controller + ResponseBody 的组合注解；前者用于定义控制器类，后者用于将方法返回值转换为 JSON 格式
@RequestMapping("/oauth")
@Controller
public class OAuth2LoginController {
    @Resource
    private GitHubOAuth2Config gitHubOAuth2Config;
    @Resource
    private RestTemplateConfig restTemplateConfig;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private UserService userService;

    @GetMapping("/redirect/github")
    String redirect(@RequestParam("code") String code) {
        String url = "https://github.com/login/oauth/access_token";

        LinkedMultiValueMap<String,String> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("client_id", gitHubOAuth2Config.getClientId());
        paramMap.add("client_secret", gitHubOAuth2Config.getClientSecret());
        paramMap.add("code", code);

        //RestTemplate restTemplate = new RestTemplate();
        //String content = restTemplate.postForObject(url, paramMap, String.class); // 请求HTTPS报错 unable to find valid certification path to requested target
        String content = restTemplateConfig.restTemplateHttps().postForObject(url, paramMap, String.class);

        // 处理数据 获取 access_token
        int start = content.indexOf("=");
        int end = content.indexOf("&");

        String access_token = content.substring(start+1,end);

        // 通过 access_token 获取第三方用户信息
        String url2 = "https://api.github.com/user";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "token " + access_token);
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<GithubUser> githubUserResponseEntity = restTemplateConfig.restTemplateHttps().exchange(url2, HttpMethod.GET, httpEntity, GithubUser.class);
        GithubUser githubUser = githubUserResponseEntity.getBody();
        System.out.println(githubUser.getLogin()+" github 正在登录");


        // 查询数据库是否有该用户，有则直接登录(传数据给前端)，没有则跳转到第三方绑定页面
        User user = userService.getOne(new QueryWrapper<User>().eq("github_account_bing_id", githubUser.getLogin()));
        if (user != null) {
            StpUtil.login(user.getId());
            String token = StpUtil.getTokenInfo().getTokenValue();
            System.out.println("第三方账号[github]登录成功："+ user.getUsername());
            // 存到Redis
            stringRedisTemplate.opsForValue().set(token, JSON.toJSONString(user), 3, TimeUnit.MINUTES);
            return "redirect:http://localhost:5173?token=" + token;
        } else {
            String keyName = "oauth_"+ githubUser.getLogin();
            String gitHubUserJSON = JSON.toJSONString(githubUser);
            stringRedisTemplate.opsForValue().set(keyName, gitHubUserJSON, 3, TimeUnit.HOURS);
            // 重定向至第三方登录页面，并在 URL 中附带参数 （由用户来决定绑定已有账号或者注册)
            return "redirect:http://localhost:5173/thirdLogin?okey=" + keyName + "&type=GitHub";
        }


        // 使前端重定向至首页
        //return "redirect:/thirdLogin";
    }
}

/*
        //System.out.println("content: " + content);
        // 小结(搞了很久.服了)：http 请求返回的永远都是 null，https 请求返回的是正常的数据
        // 但是使用 HTTPS 请求时，会报错：unable to find valid certification path to requested target (证书认证失败)
        // 为了解决这个问题，需要忽略证书认证或者添加证书 (这边使用忽略证书认证的方式)
        // 对于springBoot3.x，需要引入依赖：org.apache.httpcomponents:httpclient5
        // 然后在 RestTemplateConfig.java 中添加一个方法，用于创建一个忽略证书认证的 RestTemplate 对象
        // 然后在这里使用这个对象进行请求 (https://blog.csdn.net/u011974797/article/details/132192109)
* */
package cn.tockey.controller;


import cn.tockey.domain.User;
import cn.tockey.service.UserService;
import cn.tockey.vo.BaseResult;
import cn.tockey.vo.UserRegVo;
import cn.tockey.vo.UserVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    // 登录
    @PostMapping("/login")
    BaseResult<String> login(@RequestBody UserVo loginUser) {
        User user = userService.login(loginUser);
        if (user != null) {
            String token = userService.generateToken(user);
            return BaseResult.ok("登录成功", user).append("token", token);
        }
        return BaseResult.error("登录失败,用户名或密码错误");
    }

    // 注册
    @PostMapping("/register")
    BaseResult<String> register(@RequestBody UserRegVo registerUser) {
        boolean registerResult = userService.register(registerUser);
        if (registerResult) {
            return BaseResult.ok("注册成功");
        } else {
            return BaseResult.error("注册失败");
        }
    }

    // 检查用户名是否存在
    @GetMapping("/checkUsername/{username}")
    BaseResult<Boolean> checkUsername(@PathVariable String username) {
        User user = userService.getOne(new QueryWrapper<User>().eq("username", username));
        if (user != null) {
            return BaseResult.ok("用户名已存在", true);
        } else {
            return BaseResult.ok("用户名不存在", false);
        }
    }

    // 根据ID获取用户
    @GetMapping("/{id}")
    BaseResult<User> getUserInfoById(@PathVariable String id){
        User user = userService.getOne(new QueryWrapper<User>().eq("id", id));
        if (user == null) return BaseResult.error("用户名不存在");
        return BaseResult.ok("获取成功", user);
    }

    // 搜索用户
    @GetMapping("/search/{keyword}")
    BaseResult<List<User>> searchUser(@PathVariable String keyword) {
        List<User> userList = userService.list(new QueryWrapper<User>().like("username", keyword));
        return BaseResult.ok("搜索成功", userList);
    }
}

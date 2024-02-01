package cn.tockey.controller;


import cn.tockey.domain.Role;
import cn.tockey.domain.User;
import cn.tockey.domain.UserRole;
import cn.tockey.service.RoleService;
import cn.tockey.service.UserRoleService;
import cn.tockey.service.UserService;
import cn.tockey.vo.BaseResult;
import cn.tockey.vo.UserListVo;
import cn.tockey.vo.UserRegVo;
import cn.tockey.vo.UserVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;
    @Resource
    private UserRoleService userRoleService;
    @Resource
    private RoleService roleService;

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
    BaseResult<String> register(@RequestBody User registerUser) {
        int registerResult = userService.register(registerUser);
        if (registerResult != 0) {
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

    // 获取用户列表
    @PostMapping("/list/{num}/{size}")
    BaseResult<Page<User>> getUserList(@PathVariable int num, @PathVariable int size, @RequestBody UserListVo userListVo) {
        Page<User> page = userService.getUserList(num, size, userListVo);
        return BaseResult.ok("获取成功", page);
    }

    // 添加用户
    @PostMapping
    BaseResult<String> addUser(@RequestBody User user) {
        int saved = userService.register(user);
        // 处理角色关联
        if (user.getRoleList() != null && !user.getRoleList().isEmpty()) {
            for (Role role : user.getRoleList()) {
                userRoleService.save(new UserRole(user.getId(), role.getId()));
            }
        }

        if (saved != 0) return BaseResult.ok("添加成功");
        return BaseResult.error("添加失败");
    }

    // 修改用户
    @PutMapping
    BaseResult<String> updateUser(@RequestBody User user) {
        boolean updated = userService.updateById(user);
        // 处理角色关联
        if (user.getRoleList() != null ) {
            // 先删除关联
            userRoleService.remove(new QueryWrapper<UserRole>().eq("uid", user.getId()));
            // 再添加关联
            for (Role role : user.getRoleList()) {
                userRoleService.save(new UserRole(user.getId(), role.getId()));
            }
        }
        if (updated) return BaseResult.ok("修改成功");
        return BaseResult.error("修改失败");
    }

    // 删除用户
    @DeleteMapping("/{id}")
    BaseResult<String> deleteUser(@PathVariable String id) {
        // 检查关联角色
        List<UserRole> userRoleRelations = userRoleService.list(new QueryWrapper<UserRole>().eq("uid", id));
        ArrayList<String> UserRoleNames = new ArrayList<>();
        for (UserRole userRoleRelation : userRoleRelations) {
            Role role = roleService.getOne(new QueryWrapper<Role>().eq("id", userRoleRelation.getRid()));
            UserRoleNames.add(role.getName());
        }
        if (!userRoleRelations.isEmpty()) return BaseResult.error("删除失败,该用户已被角色:" + UserRoleNames.stream().map(name->"【"+name+"】").collect(Collectors.joining()) + "关联");

        boolean removed = userService.removeById(id);
        if (removed) return BaseResult.ok("删除成功");
        return BaseResult.error("删除失败");
    }
}

package cn.tockey.controller;



import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.tockey.config.WebSocketServer;
import cn.tockey.domain.Role;
import cn.tockey.domain.User;
import cn.tockey.domain.UserMessage;
import cn.tockey.domain.UserRole;
import cn.tockey.service.RoleService;
import cn.tockey.service.UserRoleService;
import cn.tockey.service.UserService;
import cn.tockey.vo.BaseResult;
import cn.tockey.vo.UserListVo;
import cn.tockey.vo.UserVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

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
            User userDetail = userService.getUserInfoById(user.getId());
            System.out.println("用户登录："+user);
            //String token = userService.generateToken(userDetail); // 生成 token

            // sa-token
            StpUtil.login(userDetail.getId()); // 登录
            SaTokenInfo saToken = StpUtil.getTokenInfo();

            List<String> permissionList = StpUtil.getPermissionList();
            List<String> roleList = StpUtil.getRoleList();

            System.out.println("token:"+saToken.getTokenValue());
            return BaseResult.ok("登录成功", userDetail).append("token", saToken.getTokenValue()).append("permissionList", permissionList).append("roleList", roleList);
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
        User user = userService.getUserInfoById(id);
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

    // 根据ID列表获取用户列表 controller
    @PostMapping("/list")
    BaseResult<List<User>> getUserListByIdList(@RequestBody List<String> ids){
        List<User> userList = userService.getUserListByIdList(ids);
        return BaseResult.ok("获取成功",userList);
    }

    // 根据班级ID获取成员列表
    @GetMapping("/list/byCid/{cid}")
    BaseResult<List<User>> getUserListByCid(@PathVariable Integer cid){
        List<User> list = userService.getUserListByCid(cid);
        return BaseResult.ok("获取成功",list);
    }

    // 【聊天记录】
    // 新增聊天记录
    @PostMapping("/chatRecord")
    BaseResult<String> addChatRecord(@RequestBody UserMessage userMessage) {
        int saved = userService.addChatRecord(userMessage);
        if (saved != 0) return BaseResult.ok("添加成功");
        return BaseResult.error("添加失败");
    }
    // 获取聊天记录
    @GetMapping("/chatRecord/{fromUid}/{toUid}")
    BaseResult<List<UserMessage>> getChatRecord(@PathVariable String fromUid, @PathVariable String toUid) {
        List<UserMessage> list = userService.getChatRecord(fromUid, toUid);
        return BaseResult.ok("获取成功", list);
    }

    // 将未读消息置为已读
    @PutMapping("/chatRecord/read/{fromUid}/{toUid}")
    BaseResult<String> setRead(@PathVariable String fromUid, @PathVariable String toUid) {
        int updated = userService.setRead(fromUid, toUid);
        if (updated != 0) return BaseResult.ok("ok");
        return BaseResult.ok("ok2");
    }

    // 获取未读消息
    @GetMapping("/chatRecord/unread/{uid}")
    BaseResult<List<UserMessage>> getUnreadMessage(@PathVariable String uid) {
        List<UserMessage> list = userService.getUnreadMessage(uid);
        return BaseResult.ok("获取成功", list);
    }

    // 获取活跃用户列表 前5
    @GetMapping("/activeUserList")
    BaseResult<List<User>> getActiveUserList() {
        List<User> list = userService.getActiveUserList();
        return BaseResult.ok("获取成功", list);
    }


    // sa-token 测试
    // 测试登录，浏览器访问： http://localhost:9081/user/doLogin?username=zhang&password=123456
    @RequestMapping("doLogin")
    public String doLogin(String username, String password) {
        // 此处仅作模拟示例，真实项目需要从数据库中查询数据进行比对
        if("zhang".equals(username) && "123456".equals(password)) {
            StpUtil.login(10001);
            return "登录成功";
        }
        return "登录失败";
    }

    // 查询登录状态，浏览器访问： http://localhost:9081/user/isLogin
    @RequestMapping("isLogin")
    public String isLogin() {
        return "当前会话是否登录：" + StpUtil.isLogin();
    }
}

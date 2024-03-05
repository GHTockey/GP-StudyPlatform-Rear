package cn.tockey.service.impl;

import cn.tockey.domain.Classes;
import cn.tockey.domain.Role;
import cn.tockey.domain.User;
import cn.tockey.domain.UserRole;
import cn.tockey.mapper.UserMapper;
import cn.tockey.service.ClassesService;
import cn.tockey.service.RoleService;
import cn.tockey.service.UserRoleService;
import cn.tockey.service.UserService;
import cn.tockey.config.JwtProperties;
import cn.tockey.utils.JwtUtils;
import cn.tockey.vo.UserListVo;
import cn.tockey.vo.UserVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private JwtProperties jwtProperties;
    @Resource
    private UserRoleService userRoleService;
    @Resource
    private RoleService roleService;
    @Resource
    private ClassesService classesService;

    // 登录
    @Override
    public User login(UserVo loginUser) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", loginUser.getUsername());
        queryWrapper.eq("password", loginUser.getPassword());
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) return null;
        user.setPassword(null);
        return user;
    }

    // 注册
    @Override
    public Integer register(User registerUser) {
        int inserted = userMapper.insert(registerUser);
        return inserted;
    }

    // 生成token
    @Override
    public String generateToken(User user) {
        String token = JwtUtils.generateToken(user, jwtProperties.getExpire(), jwtProperties.getPrivateKey());
        return token;
    }

    // 获取用户列表
    @Override
    public Page<User> getUserList(int pageNum, int pageSize, UserListVo userListVo) {
        Page<User> page = new Page<>(pageNum, pageSize);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();


        if (userListVo.getUsername() != null && !userListVo.getUsername().isEmpty()) {
            queryWrapper.like("users.username", userListVo.getUsername());
        }
        if (userListVo.getRid() != null) {
            queryWrapper.eq("user_role.rid", userListVo.getRid());
        }
        userMapper.getUserByRid(page, queryWrapper);


        // 关联
        for (User user : page.getRecords()) {
            List<UserRole> userRoleList = userRoleService.getUserRole(user.getId());
            ArrayList<Role> roleList = new ArrayList<>();
            for (UserRole userRole : userRoleList) {
                Role role = roleService.getOne(new QueryWrapper<Role>().eq("id", userRole.getRid()));
                roleList.add(role);
            }
            if (!roleList.isEmpty()) user.setRoleList(roleList);
        }

        return page;
    }

    // 根据ID获取用户信息 serviceImpl
    @Override
    public User getUserInfoById(String id) {
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("id", id));
        //System.out.println(user);
        // 关联班级
        if(user != null){
            Classes classes = classesService.getCLassesByUid(user.getId());
            user.setClasses(classes);
        }
        return user;
    }
}

package cn.tockey.service.impl;

import cn.tockey.config.WebSocketServer;
import cn.tockey.domain.*;
import cn.tockey.mapper.UserClassesMapper;
import cn.tockey.mapper.UserMapper;
import cn.tockey.mapper.UserMessageMapper;
import cn.tockey.service.ClassesService;
import cn.tockey.service.RoleService;
import cn.tockey.service.UserRoleService;
import cn.tockey.service.UserService;
import cn.tockey.config.JwtProperties;
import cn.tockey.utils.JwtUtils;
import cn.tockey.vo.UserListVo;
import cn.tockey.vo.UserVo;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
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
    @Resource
    private UserClassesMapper userClassesMapper;
    @Resource
    private UserMessageMapper userMessageMapper;
    @Resource
    private WebSocketServer webSocketServer;

    // 登录
    @Override
    public User login(UserVo loginUser) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", loginUser.getUsername());
        queryWrapper.eq("password", loginUser.getPassword());
        User user = userMapper.selectOne(queryWrapper);
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
        // 关联班级
        if (user != null) {
            Classes classes = classesService.getCLassesByUid(user.getId());
            user.setClasses(classes);
        }
        return user;
    }

    // 根据ID列表获取用户列表 serviceImpl
    @Override
    public List<User> getUserListByIdList(List<String> ids) {
        ArrayList<User> users = new ArrayList<>();
        for (String id : ids) {
            users.add(
                    userMapper.selectOne(
                            new QueryWrapper<User>().eq("id", id)
                    )
            );
        }
        return users;
    }

    // 根据班级ID获取成员列表 serviceImpl
    @Override
    public List<User> getUserListByCid(Integer cid) {
        List<UserClasses> userClassesList = userClassesMapper.selectList(
                new QueryWrapper<UserClasses>().eq("cid", cid)
        );
        ArrayList<User> list = new ArrayList<>();
        for (UserClasses userClasses : userClassesList) {
            list.add(
                    userMapper.selectOne(new QueryWrapper<User>().eq("id",userClasses.getUid()))
            );
        }
        return list;
    }

    // 新增聊天记录 serviceImpl
    @Override
    public Integer addChatRecord(UserMessage userMessage) {
        return userMessageMapper.insert(userMessage);
    }

    // 获取聊天记录 serviceImpl
    @Override
    public List<UserMessage> getChatRecord(String fromUid, String toUid) {
        QueryWrapper<UserMessage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("receiver_id", toUid);
        queryWrapper.eq("sender_id", fromUid);
        queryWrapper.or();
        queryWrapper.eq("receiver_id", fromUid);
        queryWrapper.eq("sender_id", toUid);
        queryWrapper.orderByAsc("timestamp");
        return userMessageMapper.selectList(queryWrapper);
    }

    // 将未读消息置为已读 serviceImpl
    @Override
    public Integer setRead(String fromUid, String toUid) {
        // 通过 socket 通知发送者发送的消息已读
        UserMessage userMessageFB = new UserMessage();
        userMessageFB.setReceiverId(fromUid); // 目标：发送者
        HashMap<String, Object> fbData = new HashMap<>();
        fbData.put("msg", "你发送给"+toUid+"的消息已读");
        fbData.put("id", toUid);
        userMessageFB.setMessage(JSON.toJSONString(fbData));
        userMessageFB.setType(4);
        webSocketServer.sendToOne(userMessageFB);

        QueryWrapper<UserMessage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("receiver_id", toUid);
        queryWrapper.eq("sender_id", fromUid);
        queryWrapper.eq("is_read", 1);
        UserMessage userMessage = new UserMessage();
        userMessage.setIsRead(0);
        return userMessageMapper.update(userMessage, queryWrapper);
    }

    // 获取未读消息 serviceImpl
    @Override
    public List<UserMessage>
    getUnreadMessage(String uid) {
        QueryWrapper<UserMessage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("receiver_id", uid);
        queryWrapper.eq("is_read", 1);
        return userMessageMapper.selectList(queryWrapper);
    }

    // 获取活跃用户列表 前5 serviceImpl todo 待实现
    @Override
    public List<User> getActiveUserList() {
        return null;
    }
}

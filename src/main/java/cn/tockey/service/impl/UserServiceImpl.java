package cn.tockey.service.impl;

import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.stp.StpUtil;
import cn.tockey.config.WebSocketServer;
import cn.tockey.domain.*;
import cn.tockey.mapper.UserClassesMapper;
import cn.tockey.mapper.UserMapper;
import cn.tockey.mapper.UserMessageMapper;
import cn.tockey.mapper.UserVocabularyMapper;
import cn.tockey.service.ClassesService;
import cn.tockey.service.RoleService;
import cn.tockey.service.UserRoleService;
import cn.tockey.service.UserService;
import cn.tockey.config.JwtProperties;
import cn.tockey.utils.JwtUtils;
import cn.tockey.vo.OAuthRegisterUserVo;
import cn.tockey.vo.UserListVo;
import cn.tockey.vo.UserVo;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
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
    @Resource
    private UserVocabularyMapper userVocabularyMapper;
    @Resource
    private StringRedisTemplate stringRedisTemplate;


    // 关联处理程序
    private void relevanceHandler(User user, Boolean relevanceClasses, Boolean relevanceRole) {
        if (relevanceClasses) {
            // 关联班级
            Classes classes = classesService.getCLassesByUid(user.getId());
            user.setClasses(classes);
        }
        if (relevanceRole) {
            // 关联角色
            List<UserRole> userRoleList = userRoleService.getUserRole(user.getId());
            ArrayList<Role> roleList = new ArrayList<>();
            for (UserRole userRole : userRoleList) {
                Role role = roleService.getOne(new QueryWrapper<Role>().eq("id", userRole.getRid()));
                roleList.add(role);
            }
            user.setRoleList(roleList);
        }
    }


    // 登录
    @Override
    public User login(UserVo loginUser) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", loginUser.getUsername());
        //queryWrapper.eq("password", loginUser.getPassword());
        User user = userMapper.selectOne(queryWrapper);
        if (user != null) {
            // 密码是否匹配
            if (BCrypt.checkpw(loginUser.getPassword(), user.getPassword())) {
                // 关联
                relevanceHandler(user, true, true);
            } else {
                user = null;
            }
        }
        return user;
    }

    // 注册
    @Override
    public Integer register(User registerUser) {
        // 加密
        registerUser.setPassword(BCrypt.hashpw(registerUser.getPassword(), BCrypt.gensalt()));
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
            relevanceHandler(user, true, true);
        }

        return page;
    }

    // 根据ID获取用户信息 serviceImpl
    @Override
    public User getUserInfoById(String id) {
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("id", id));
        if (user != null) {
            // 关联角色
            relevanceHandler(user, true, true);

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

    // 获取活跃用户列表 前5 serviceImpl
    @Override
    public List<User> getActiveUserList() {
        // 得到根据学习数最多desc排序的 userVocabulary 列表
        List<UserVocabulary> mostStudyVocList = userVocabularyMapper.getMostStudyVocList();
        System.out.println(mostStudyVocList);
        ArrayList<User> list = new ArrayList<>();
        for (UserVocabulary userVocabulary : mostStudyVocList) {
            User user = userMapper.selectOne(new QueryWrapper<User>().eq("id", userVocabulary.getUid()));
            user.setStudyTotal(userVocabulary.getStudyTotal());
            list.add(user);
        }
        return list;
    }

    // 第三方账号绑定 serviceImpl
    @Override
    public Integer oAuthAccountBinding(User oAuthUser, String oKey, String type) {
        User targerUser = userMapper.selectOne(new QueryWrapper<User>().eq("username", oAuthUser.getUsername()));
        // 进行绑定
        if (type.equalsIgnoreCase("GITHUB")) {
            GithubUser githubUser = JSON.parseObject(stringRedisTemplate.opsForValue().get(oKey), GithubUser.class);
            targerUser.setGithubAccountBingId(githubUser.getLogin());
        } else if (type.equalsIgnoreCase("GITEE")) {
            GiteeUser giteeUser = JSON.parseObject(stringRedisTemplate.opsForValue().get(oKey), GiteeUser.class);
            targerUser.setGiteeAccountBingId(giteeUser.getLogin());
        } else {
            return 0;
        }

        return userMapper.updateById(targerUser);
    }

    // 通过 token 获取用户信息 serviceImpl
    @Override
    public User getUserInfoByToken(String token) {
        User redisUser = JSON.parseObject(stringRedisTemplate.opsForValue().get(token), User.class);
        stringRedisTemplate.delete(token);
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("id", redisUser.getId()));
        // 关联
        relevanceHandler(user, true, true);
        return user;
    }

    // OAuth 注册登录 serviceImpl
    @Override
    public User oAuthRegisterLogin(OAuthRegisterUserVo oAuthRegisterUserVo, String oKey, String type) {
        User newUser = new User();
        newUser.setUsername(oAuthRegisterUserVo.getUsername());
        // 加密
        newUser.setPassword(BCrypt.hashpw(oAuthRegisterUserVo.getPassword(), BCrypt.gensalt()));// 密码是否匹配：BCrypt.checkpw(明文,密文)
        newUser.setAvatar(oAuthRegisterUserVo.getAvatar());
        newUser.setEmail(oAuthRegisterUserVo.getEmail());
        if(type.equalsIgnoreCase("GITHUB")) newUser.setGithubAccountBingId(oAuthRegisterUserVo.getUsername());
        if(type.equalsIgnoreCase("GITEE")) newUser.setGiteeAccountBingId(oAuthRegisterUserVo.getUsername());
        // ...
        userMapper.insert(newUser);

        // 删除 Redis 缓存
        stringRedisTemplate.delete(oKey);
        return newUser;
    }

    // 检查用户名是否可用 serviceImpl
    @Override
    public Boolean checkUsername(String username) {
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("username", username));
        return user == null;
    }


    // security 测试
    //@Resource
    //private DBUserDetailsManager dbUserDetailsManager;
    //
    //@Override
    //public void saveUserDetails(User user) {
    //    //System.out.println("加密前：");
    //    //System.out.println(user.getPassword());
    //
    //    UserDetails userDetails = org.springframework.security.core.userdetails.User
    //            .withUsername(user.getUsername())
    //            .password(new BCryptPasswordEncoder().encode(user.getPassword()))
    //            .build();
    //    dbUserDetailsManager.createUser(userDetails);
    //
    //    //System.out.println("加密后：");
    //    //System.out.println(userDetails.getPassword());
    //}
}

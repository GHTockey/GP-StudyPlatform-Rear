package cn.tockey.service.impl;

import cn.tockey.config.JwtProperties;
import cn.tockey.domain.User;
import cn.tockey.mapper.UserMapper;
import cn.tockey.repository.UserRepository;
import cn.tockey.service.UserService;
import cn.tockey.utils.JwtUtils;
import cn.tockey.vo.UserRegVo;
import cn.tockey.vo.UserVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private JwtProperties jwtProperties;
    @Resource
    private UserRepository userRepository;

    @Override
    public User login(UserVo loginUser){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", loginUser.getUsername());
        queryWrapper.eq("password", loginUser.getPassword());
        User user = userMapper.selectOne(queryWrapper);
        user.setPassword(null);
        return user;
    }

    @Override
    public boolean register(UserRegVo registerUser) {
        userRepository.insertNewUserProcedure(registerUser.getUsername(), registerUser.getPassword(), registerUser.getAvatar(), registerUser.getEmail());
        return true;
    }

    @Override
    public String generateToken(User user) {
        String token = JwtUtils.generateToken(user.getUsername(), jwtProperties.getExpire(), jwtProperties.getPrivateKey());
        return token;
    }
}
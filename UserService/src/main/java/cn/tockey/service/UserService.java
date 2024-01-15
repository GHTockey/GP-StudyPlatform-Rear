package cn.tockey.service;

import cn.tockey.domain.User;
import cn.tockey.vo.UserRegVo;
import cn.tockey.vo.UserVo;
import com.baomidou.mybatisplus.extension.service.IService;

public interface UserService extends IService<User> {
    // 登录
    User login(UserVo loginUser);

    // 注册
    boolean register(UserRegVo registerUser);

    // 生成token
    String generateToken(User user);

}

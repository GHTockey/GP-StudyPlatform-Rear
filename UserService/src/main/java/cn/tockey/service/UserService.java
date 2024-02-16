package cn.tockey.service;

import cn.tockey.domain.User;
import cn.tockey.vo.UserListVo;
import cn.tockey.vo.UserRegVo;
import cn.tockey.vo.UserVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

public interface UserService extends IService<User> {
    // 登录
    User login(UserVo loginUser);

    // 注册
    Integer register(User user);

    // 生成token
    String generateToken(User user);

    // 获取用户列表
    Page<User> getUserList(int pageNum, int pageSize, UserListVo userListVo);

    // 根据ID获取用户信息
    User getUserInfoById(String id);
}

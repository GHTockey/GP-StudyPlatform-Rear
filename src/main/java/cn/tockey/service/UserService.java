package cn.tockey.service;

import cn.tockey.domain.User;
import cn.tockey.domain.UserMessage;
import cn.tockey.vo.OAuthRegisterUserVo;
import cn.tockey.vo.UserListVo;
import cn.tockey.vo.UserVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface UserService extends IService<User> {
    // 登录
    User login(UserVo loginUser);
    // 注册
    Integer register(User user);
    // 生成token
    String generateToken(User user);
    // 按条件获取用户列表
    Page<User> getUserList(int pageNum, int pageSize, UserListVo userListVo);
    // 根据ID获取用户信息
    User getUserInfoById(String id);
    // 根据ID列表获取用户列表
    List<User> getUserListByIdList(List<String> ids);
    // 根据班级ID获取成员列表
    List<User> getUserListByCid(Integer cid);
    // 新增聊天记录
    Integer addChatRecord(UserMessage userMessage);
    // 获取聊天记录
    List<UserMessage> getChatRecord(String fromUid, String toUid);
    // 将未读消息置为已读
    Integer setRead(String fromUid, String toUid);
    // 获取未读消息
    List<UserMessage> getUnreadMessage(String uid);
    // 获取学习数最多的用户列表
    List<User> getActiveUserList();
    // 第三方账号绑定
    Integer oAuthAccountBinding(User oAuthUser, String oKey, String type);
    // 通过 token 获取用户信息
    User getUserInfoByToken(String token);
    // OAuth 注册登录
    User oAuthRegisterLogin(OAuthRegisterUserVo oAuthRegisterUserVo, String oKey, String type);
    // 检查用户名是否可用
    Boolean checkUsername(String username);

    // security 测试
    //void saveUserDetails(User user);
}

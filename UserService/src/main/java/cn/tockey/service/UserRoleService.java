package cn.tockey.service;

import cn.tockey.domain.UserRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author tockey
 * @since 2024-01-16
 */
public interface UserRoleService extends IService<UserRole> {
    // 获取角色列表
    List<UserRole> getUserRole(Integer uid);
}

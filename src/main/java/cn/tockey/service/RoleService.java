package cn.tockey.service;

import cn.tockey.domain.Role;
import cn.tockey.vo.SetRolePermVo;
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
public interface RoleService extends IService<Role> {
    // 查询角色列表
    List<Role> getRoleList();

    // 添加角色权限
    int addRole(Integer rid, SetRolePermVo setRolePermVo);
}

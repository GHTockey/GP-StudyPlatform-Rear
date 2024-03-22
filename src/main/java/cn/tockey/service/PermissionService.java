package cn.tockey.service;

import cn.tockey.domain.Permission;
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
public interface PermissionService extends IService<Permission> {
    // 根据用户id获取权限列表 service (仅type=page)
    List<Permission> getPagePermissionListByUid(String uid);
    // 根据用户id获取权限列表 service (all)
    List<Permission> getAllPermissionListByUid(String uid);

    // 根据角色id获取权限列表 service
    List<Permission> getPermissionByRid(Integer rid);

    // 添加权限
    int addPermission(Permission permission);
    // 修改权限
    int updPermission(Permission permission);

    // 根据权限id获取所有的父级id
    List<Integer> getAllPidById(Integer id);

    // 根据权限id获取所有的子级id
    List<Integer> getAllChildIdById(Integer id);

    // 获取权限列表
    List<Permission> getPermissionList();
}

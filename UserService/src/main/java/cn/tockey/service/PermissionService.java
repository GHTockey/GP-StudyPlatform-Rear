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
    // 根据角色id获取权限 service
    List<Permission> getPermissionByUid(String uid);

    // 添加权限
    int addPermission(Permission permission);

    // 根据权限id获取所有的父级id
    List<Integer> getAllPidById(Integer id);

}

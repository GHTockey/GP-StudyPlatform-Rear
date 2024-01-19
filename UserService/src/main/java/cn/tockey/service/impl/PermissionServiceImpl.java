package cn.tockey.service.impl;

import cn.tockey.domain.Permission;
import cn.tockey.mapper.PermissionMapper;
import cn.tockey.service.PermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author tockey
 * @since 2024-01-16
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {
    @Resource
    private PermissionMapper permissionMapper;

    // 根据角色id获取权限 serviceImpl
    @Override
    public List<Permission> getPermissionByUid(String uid) {
        List<Permission> permissionList = permissionMapper.getPermissionByUid(uid);
        //ArrayList<Permission> treePermissionList = new ArrayList<>();
        //// 确定根节点
        //for (Permission permission : permissionList) {
        //    if (permission.getParentId().equals(0)) {
        //        treePermissionList.add(permission);
        //    }
        //}
        //// 去递归添加子节点
        //for (Permission parentPermission : treePermissionList) {
        //    addPermissionToTreeRecursive(parentPermission, permissionList);
        //}
        //return treePermissionList;

        return permissionList;
    }


}

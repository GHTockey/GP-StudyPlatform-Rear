package cn.tockey.service.impl;

import cn.tockey.domain.PermIcon;
import cn.tockey.domain.Permission;
import cn.tockey.domain.RolePermission;
import cn.tockey.mapper.PermissionMapper;
import cn.tockey.mapper.RolePermissionMapper;
import cn.tockey.service.IconService;
import cn.tockey.service.PermIconService;
import cn.tockey.service.PermissionService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    @Resource
    private RolePermissionMapper rolePermissionMapper;
    @Resource
    private IconService iconService;
    @Resource
    private PermIconService permIconService;

    // 递归获取所有父级id
    private List<Integer> getPermPidHandler(int id, List<Integer> ids){
        Permission perm = permissionMapper.selectOne(new QueryWrapper<Permission>().eq("id", id));
        if(perm.getParentId() != 0){
            getPermPidHandler(perm.getParentId(), ids);
        }
        ids.add(perm.getParentId());
        return ids;
    }
    // 递归获取所有子级id
    private List<Integer> getPermCidHandler(int id, List<Integer> ids){
        List<Permission> permissionList = permissionMapper.selectList(new QueryWrapper<Permission>().eq("parent_id", id));
        if(permissionList != null){
            for (Permission permission : permissionList) {
                ids.add(permission.getId());
                getPermCidHandler(permission.getId(), ids);
            }
            //ids.add(permission.getId());
            //getPermCidHandler(permission.getId(), ids);
        }
        return ids;
    }
    // 获取关联图标 function
    private void permGetRelevanceIcon(List<Permission> permissionList) {
        for (Permission permission : permissionList) {
            PermIcon permIcon = permIconService.getPermIconIdByPid(String.valueOf(permission.getId()));
            if (permIcon != null) permission.setIcon(iconService.getIconById(permIcon.getIconId()));
        }
    }
    // 添加关联图标 function
    private void permAddRelevanceIcon(Permission permission) {
        if(permission.getIcon() != null){
            PermIcon permIcon = new PermIcon(permission.getId(), permission.getIcon().getId());
            permIconService.addPermIcon(permIcon);
        }
    }


    // 根据用户id获取权限列表 serviceImpl
    @Override
    public List<Permission> getPermissionByUid(String uid) {
        // 前端自行递归处理数据 24 01-19 22:30
        List<Permission> permissionList = permissionMapper.getPermissionByUid(uid);
        // 关联图标
        permGetRelevanceIcon(permissionList);
        return permissionList;
    }

    // 根据角色id获取权限列表 serviceImpl
    @Override
    public List<Permission> getPermissionByRid(Integer rid) {
        List<RolePermission> rolePermList = rolePermissionMapper.selectList(new QueryWrapper<RolePermission>().eq("rid", rid));
        ArrayList<Permission> permissionList = new ArrayList<>();
        for (RolePermission rolePermission : rolePermList) {
            Permission permission = permissionMapper.selectOne(new QueryWrapper<Permission>().eq("id", rolePermission.getPid()));
            permission.setHalfCheck(rolePermission.getHalfCheck());
            permissionList.add(permission);
        }
        // 关联图标
        permGetRelevanceIcon(permissionList);
        return permissionList;
    }

    // 添加权限 serviceImpl
    @Override
    public int addPermission(Permission permission) {
        int i = permissionMapper.insert(permission);
        // 将关联表中的父级id设置为半选中 ( todo 问题: 只会设置父级，不会设置父级的父级...)
        if(permission.getParentId() != 0){
            RolePermission rolePermission = new RolePermission();
            rolePermission.setHalfCheck(1);
            rolePermissionMapper.update(
                    rolePermission,
                    new QueryWrapper<RolePermission>().eq("pid", permission.getParentId())
            );
        }
        // 添加关联图标
        //iconService.removePermIcon(permission.getId()); // 先删除原有关联
        // 先检查是否有图标
        PermIcon permIcon = permIconService.getOne(new QueryWrapper<PermIcon>().eq("perm_id", permission.getId()));
        if(permIcon != null){
            permIconService.removePermIcon(String.valueOf(permission.getId())); // 删除原有关联
        }
        permAddRelevanceIcon(permission);
        return i;
    }

    // 修改权限 serviceImpl
    @Override
    public int updPermission(Permission permission) {
        int i = permissionMapper.updateById(permission);
        // 添加关联图标
        //iconService.removePermIcon(permission.getId()); // 先删除原有关联
        // 先检查是否有图标
        PermIcon permIcon = permIconService.getOne(new QueryWrapper<PermIcon>().eq("perm_id", permission.getId()));
        if(permIcon != null){
            permIconService.removePermIcon(String.valueOf(permission.getId())); // 删除原有关联
        }
        permAddRelevanceIcon(permission);
        return i;
    }

    // 根据权限id获取所有的父级id serviceImpl
    @Override
    public List<Integer> getAllPidById(Integer id) {
        ArrayList<Integer> ids = new ArrayList<>();
        List<Integer> permPidHandler = getPermPidHandler(id, ids);
        return permPidHandler;
    }

    // 根据权限id获取所有的子级id serviceImpl
    @Override
    public List<Integer> getAllChildIdById(Integer id){
        ArrayList<Integer> ids = new ArrayList<>();
        List<Integer> permCidHandler = getPermCidHandler(id, ids);
        return permCidHandler;
    }

    @Override
    public List<Permission> getPermissionList() {
        List<Permission> permissionList = permissionMapper.selectList(null);
        // 关联图标
        permGetRelevanceIcon(permissionList);
        return permissionList;
    }
}

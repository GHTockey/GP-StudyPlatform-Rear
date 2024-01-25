package cn.tockey.service.impl;

import cn.tockey.domain.Permission;
import cn.tockey.domain.RolePermission;
import cn.tockey.mapper.PermissionMapper;
import cn.tockey.mapper.RolePermissionMapper;
import cn.tockey.service.PermissionService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
    @Resource
    private RolePermissionMapper rolePermissionMapper;

    // 根据用户id获取权限列表 serviceImpl
    @Override
    public List<Permission> getPermissionByUid(String uid) {
        // 前端自行递归处理数据 24 01-19 22:30
        List<Permission> permissionList = permissionMapper.getPermissionByUid(uid);
        return permissionList;
    }

    // 根据角色id获取权限列表 serviceImpl
    @Override
    public List<Permission> getPermissionByRid(Integer rid) {
        List<RolePermission> rolePermList = rolePermissionMapper.selectList(new QueryWrapper<RolePermission>().eq("rid", rid));
        ArrayList<Permission> permissionList = new ArrayList<>();
        for (RolePermission rolePermission : rolePermList) {
            Permission permission = permissionMapper.selectOne(new QueryWrapper<Permission>().eq("id", rolePermission.getPid()));
            permissionList.add(permission);
        }
        return permissionList;
    }

    @Override
    public int addPermission(Permission permission) {
        int i = permissionMapper.insert(permission);
        return i;
    }

    @Override
    public List<Integer> getAllPidById(Integer id) {
        ArrayList<Integer> ids = new ArrayList<>();
        List<Integer> permPidHandler = getPermPidHandler(id, ids);
        return permPidHandler;
    }

    private List<Integer> getPermPidHandler(int id, List<Integer> ids){
        Permission perm = permissionMapper.selectOne(new QueryWrapper<Permission>().eq("id", id));
        if(perm.getParentId() != 0){
            getPermPidHandler(perm.getParentId(), ids);
        }
        ids.add(perm.getParentId());
        return ids;
    };
}

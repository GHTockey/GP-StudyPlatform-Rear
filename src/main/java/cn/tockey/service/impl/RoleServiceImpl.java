package cn.tockey.service.impl;

import cn.tockey.domain.Role;
import cn.tockey.domain.RolePermission;
import cn.tockey.domain.UserRole;
import cn.tockey.mapper.RoleMapper;
import cn.tockey.mapper.UserRoleMapper;
import cn.tockey.service.RolePermissionService;
import cn.tockey.service.RoleService;
import cn.tockey.vo.SetRolePermVo;
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
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private UserRoleMapper userRoleMapper;
    @Resource
    private RolePermissionService rolePermissionService;


    @Override
    public List<Role> getRoleList() {
        List<Role> roleList = roleMapper.selectList(null);
        // 关联
        for (Role role : roleList) {
            ArrayList<String> userIds = new ArrayList<>();
            List<UserRole> userRoleList = userRoleMapper.selectList(new QueryWrapper<UserRole>().eq("rid", role.getId()));
            for (UserRole userRole : userRoleList) {
                userIds.add(userRole.getUid());
            }
            role.setUserIds(userIds);
        }
        return roleList;
    }

    @Override
    public int addRole(Integer rid, SetRolePermVo setRolePermVo) {
        try {
            // 删除旧权限
            rolePermissionService.remove(new QueryWrapper<RolePermission>().eq("rid", rid));
            // 添加新权限 (选中的)
            for (Integer selectedId : setRolePermVo.getSelectedIds()) {
                RolePermission rolePerm = new RolePermission();
                rolePerm.setRid(rid);
                rolePerm.setPid(selectedId);
                rolePerm.setHalfCheck(0);
                rolePermissionService.save(rolePerm);
            }
            // 添加新权限 (半选中的)
            for (Integer halfCheckId : setRolePermVo.getHalfCheckIds()) {
                RolePermission rolePerm = new RolePermission();
                rolePerm.setRid(rid);
                rolePerm.setPid(halfCheckId);
                rolePerm.setHalfCheck(1);
                rolePermissionService.save(rolePerm);
            }
            return 1;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    // 根据用户ID获取角色列表 serviceImpl (仅type=page)
    @Override
    public List<Role> getRoleListByUid(String uid) {
        List<UserRole> userRoleList = userRoleMapper.selectList(new QueryWrapper<UserRole>().eq("uid", uid));
        ArrayList<Role> roleList = new ArrayList<>();
        for (UserRole userRole : userRoleList) {
            Role role = roleMapper.selectById(userRole.getRid());
            roleList.add(role);
        }
        return roleList;
    }

}

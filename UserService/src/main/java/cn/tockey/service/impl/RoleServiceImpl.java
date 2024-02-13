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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
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
@Transactional
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
}

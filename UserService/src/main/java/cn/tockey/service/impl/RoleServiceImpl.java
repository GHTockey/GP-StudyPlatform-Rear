package cn.tockey.service.impl;

import cn.tockey.domain.Role;
import cn.tockey.domain.UserRole;
import cn.tockey.mapper.RoleMapper;
import cn.tockey.mapper.UserRoleMapper;
import cn.tockey.service.RoleService;
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
}

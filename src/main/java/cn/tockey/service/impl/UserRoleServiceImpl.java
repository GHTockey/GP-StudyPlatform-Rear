package cn.tockey.service.impl;

import cn.tockey.domain.UserRole;
import cn.tockey.mapper.UserRoleMapper;
import cn.tockey.service.UserRoleService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

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
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {
    @Resource
    private UserRoleMapper userRoleMapper;

    @Override
    public List<UserRole> getUserRole(String uid) {
        return userRoleMapper.selectList(new QueryWrapper<UserRole>().eq("uid", uid));
    }
}

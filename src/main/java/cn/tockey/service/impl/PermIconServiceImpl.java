package cn.tockey.service.impl;

import cn.tockey.domain.PermIcon;
import cn.tockey.mapper.PermIconMapper;
import cn.tockey.service.PermIconService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author tockey
 * @since 2024-02-01
 */
@Service
public class PermIconServiceImpl extends ServiceImpl<PermIconMapper, PermIcon> implements PermIconService {
    @Resource
    private PermIconMapper permIconMapper;

    // 权限移除图标 serviceImpl
    @Override
    public boolean removePermIcon(String pid) {
        int i = permIconMapper.delete(new QueryWrapper<PermIcon>().eq("perm_id", pid));
        return i > 0;
    }

    // 权限使用图标 【添加】 serviceImpl
    @Override
    public boolean addPermIcon(PermIcon permIcon) {
        int i = permIconMapper.insert(permIcon);
        return i > 0;
    }

    // 获取权限使用的图标 【根据权限id】 serviceImpl
    @Override
    public PermIcon getPermIconIdByPid(String pid) {
        QueryWrapper<PermIcon> queryWrapper = new QueryWrapper<PermIcon>();
        queryWrapper.eq("perm_id", pid);
        PermIcon permIcon = permIconMapper.selectOne(queryWrapper);
        return permIcon;
    }
}

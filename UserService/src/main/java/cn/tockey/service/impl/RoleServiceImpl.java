package cn.tockey.service.impl;

import cn.tockey.domain.Role;
import cn.tockey.mapper.RoleMapper;
import cn.tockey.service.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

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


}

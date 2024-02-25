package cn.tockey.service.impl;

import cn.tockey.domain.Icon;
import cn.tockey.mapper.IconMapper;
import cn.tockey.service.IconService;
import cn.tockey.service.PermIconService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author tockey
 * @since 2024-02-01
 */
@Service
public class IconServiceImpl extends ServiceImpl<IconMapper, Icon> implements IconService {
    @Resource
    private IconMapper iconMapper;
    @Resource
    private PermIconService permIconService;

    // 获取图标列表 serviceImpl
    @Override
    public List<Icon> getIconList() {
        List<Icon> iconList = iconMapper.selectList(null);
        return iconList;
    }

    // 添加图标 serviceImpl
    @Override
    public boolean addIcon(Icon icon) {
        icon.setUpdateTime(null);
        boolean saved = iconMapper.insert(icon) > 0;
        return saved;
    }

    // 删除图标 serviceImpl
    @Override
    public boolean delIcon(Integer[] ids) {
        boolean removed = iconMapper.deleteBatchIds(Arrays.asList(ids)) > 0;
        return removed;
    }

    // 修改图标 serviceImpl
    @Override
    public boolean updIcon(Icon icon) {
        boolean updated = iconMapper.updateById(icon) > 0;
        return updated;
    }

    // 获取图标 【根据id】 serviceImpl
    @Override
    public Icon getIconById(Integer id) {
        Icon icon = iconMapper.selectById(id);
        return icon;
    }
}

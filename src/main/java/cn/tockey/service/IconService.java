package cn.tockey.service;

import cn.tockey.domain.Icon;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author tockey
 * @since 2024-02-01
 */
public interface IconService extends IService<Icon> {

    // 获取图标列表
     List<Icon> getIconList();
    // 添加图标
    boolean addIcon(Icon icon);
    // 删除图标
    boolean delIcon(Integer[] ids);
    // 修改图标
    boolean updIcon(Icon icon);
    // 获取图标 【根据id】
    Icon getIconById(Integer id);
}

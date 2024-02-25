package cn.tockey.service;

import cn.tockey.domain.PermIcon;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author tockey
 * @since 2024-02-01
 */
public interface PermIconService extends IService<PermIcon> {
    // 权限关联图标表
    // 权限移除图标
    boolean removePermIcon(String pid);
    // 权限使用图标 【添加】
    boolean addPermIcon(PermIcon permIcon);
    // 获取权限使用的图标 【根据权限id】
    PermIcon getPermIconIdByPid(String pid);
}

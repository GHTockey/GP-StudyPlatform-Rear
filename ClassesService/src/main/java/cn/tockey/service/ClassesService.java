package cn.tockey.service;

import cn.tockey.domain.Classes;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author tockey
 * @since 2024-01-15
 */
public interface ClassesService extends IService<Classes> {
    // 添加班级
    Classes addClasses(Classes classes);
    // 获取班级
    Classes getClasses(String id);
}

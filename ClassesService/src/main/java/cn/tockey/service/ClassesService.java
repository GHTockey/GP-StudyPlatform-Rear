package cn.tockey.service;

import cn.tockey.domain.Classes;
import cn.tockey.domain.UserClasses;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

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
    Classes getClassesById(String id);
    // 删除班级
    Integer delClassesById(String id);
    // 修改班级
    Integer updClasses(Classes classes);
    // 获取班级列表
    List<Classes> getClassesList();
    // 用户加入班级 【关联表】
    Integer userAddClasses(UserClasses userClasses);
}

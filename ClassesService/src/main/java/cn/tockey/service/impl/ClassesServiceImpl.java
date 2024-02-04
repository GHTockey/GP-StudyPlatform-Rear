package cn.tockey.service.impl;

import cn.tockey.domain.Classes;
import cn.tockey.domain.User;
import cn.tockey.domain.UserClasses;
import cn.tockey.feign.UserFeign;
import cn.tockey.mapper.ClassesMapper;
import cn.tockey.mapper.UserClassesMapper;
import cn.tockey.service.ClassesService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author tockey
 * @since 2024-01-15
 */
@Service
@Transactional
public class ClassesServiceImpl extends ServiceImpl<ClassesMapper, Classes> implements ClassesService {
    @Resource
    private ClassesMapper classesMapper;
    @Resource
    private UserClassesMapper userClassesMapper;
    @Resource
    private UserFeign userFeign;

    // 添加班级 serviceImpl
    @Override
    public Classes addClasses(Classes classes) {
        int i = classesMapper.insert(classes);
        if (i == 1) return classes;
        return null;
    }

    // 获取班级 serviceImpl
    @Override
    public Classes getClassesById(String id) {
        Classes classes = classesMapper.selectOne(new QueryWrapper<Classes>().eq("id", id));
        List<UserClasses> userClassesList = userClassesMapper.selectList(new QueryWrapper<UserClasses>().eq("cid", id));
        ArrayList<User> userList = new ArrayList<>();
        for (UserClasses userClasses : userClassesList) {
            User user = userFeign.getUserInfoById(userClasses.getUid()).getData();
            userList.add(user);
        }
        classes.setUserList(userList);
        return classes;
    }

    // 删除班级 serviceImpl
    @Override
    public Integer delClassesById(String id) {
        int i = classesMapper.deleteById(id);
        return i;
    }

    // 修改班级 serviceImpl
    @Override
    public Integer updClasses(Classes classes) {
        int i = classesMapper.updateById(classes);
        return i;
    }

    // 获取班级列表 serviceImpl
    @Override
    public List<Classes> getClassesList() {
        List<Classes> classesList = classesMapper.selectList(null);
        for (Classes classes : classesList) {
            // 关联创建者
            if (classes.getCreatorUid() != null) {
                User user = userFeign.getUserInfoById(classes.getCreatorUid()).getData();
                classes.setCreator(user);
            }
            // 关联成员列表
            List<UserClasses> userClassesRelevance = userClassesMapper.selectList(new QueryWrapper<UserClasses>().eq("cid", classes.getId()));
            for (UserClasses userClasses : userClassesRelevance) {
                User user = userFeign.getUserInfoById(userClasses.getUid()).getData();
                classes.getUserList().add(user);
            }
        }
        return classesList;
    }

    // 用户加入班级 【关联表】 serviceImpl
    @Override
    public Integer userAddClasses(UserClasses userClasses) {
        int inserted = userClassesMapper.insert(userClasses);
        return inserted;
    }
}

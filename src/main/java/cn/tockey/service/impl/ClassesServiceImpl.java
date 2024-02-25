package cn.tockey.service.impl;

import cn.tockey.domain.Classes;
import cn.tockey.domain.User;
import cn.tockey.domain.UserClasses;
import cn.tockey.domain.Vocabulary;
import cn.tockey.mapper.ClassesMapper;
import cn.tockey.mapper.UserClassesMapper;
import cn.tockey.mapper.UserMapper;
import cn.tockey.service.ClassesService;
import cn.tockey.service.UserService;
import cn.tockey.service.VocabularyService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

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
public class ClassesServiceImpl extends ServiceImpl<ClassesMapper, Classes> implements ClassesService {
    @Resource
    private ClassesMapper classesMapper;
    @Resource
    private UserClassesMapper userClassesMapper;
    @Resource
    //private UserService userService;
    private UserMapper userMapper;
    @Resource
    private VocabularyService vocabularyService;


    // 关联 程序
    private void relevanceHandler(Classes classes) {
        // 关联用户(成员)列表
        List<UserClasses> userClassesList = userClassesMapper.selectList(new QueryWrapper<UserClasses>().eq("cid", classes.getId()));
        for (UserClasses userClasses : userClassesList) {
            //User user = userService.getUserInfoById(userClasses.getUid());
            User user = userMapper.selectById(userClasses.getUid());
            classes.getUserList().add(user);
        }
        //关联创建者
        //User creator = userService.getUserInfoById(classes.getCreatorUid());
        User creator = userMapper.selectById(classes.getCreatorUid());
        classes.setCreator(creator);
    }



    // 添加班级 serviceImpl
    @Override
    public Classes addClasses(Classes classes) {
        int i = classesMapper.insert(classes);
        // 创建时将创建者设为成员
        UserClasses userClasses = new UserClasses(classes.getId(),classes.getCreatorUid());
        userClassesMapper.insert(userClasses);
        if (i == 1) return classes;
        return null;
    }

    // 获取班级 serviceImpl
    @Override
    public Classes getClassesById(String id) {
        Classes classes = classesMapper.selectOne(new QueryWrapper<Classes>().eq("id", id));
        if(classes != null) relevanceHandler(classes);
        return classes;
    }

    // 删除班级 serviceImpl
    @Override
    public Integer delClassesById(String id) {
         // 班级成员解除关系
        userClassesMapper.delete(new QueryWrapper<UserClasses>().eq("cid",id));
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
            relevanceHandler(classes);
        }
        return classesList;
    }

    // 用户加入班级 【关联表】 serviceImpl
    @Override
    public Integer userAddClasses(UserClasses userClasses) {
        int inserted = userClassesMapper.insert(userClasses);
        return inserted;
    }

    // 班级移除用户 【关联表】 serviceImpl
    @Override
    public Integer classesRemUser(UserClasses userClasses) {
        return userClassesMapper.delete(new QueryWrapper<UserClasses>().eq("cid", userClasses.getCid()).eq("uid",userClasses.getUid()));
    }

    // 搜索班级
    @Override
    public List<Classes> searchClassesList(String keyword) {
        List<Classes> classesList = classesMapper.selectList(new QueryWrapper<Classes>().like("name", keyword));
        for (Classes classes : classesList) {
            relevanceHandler(classes);
        }
        return classesList;
    }

    // 根据班级ID获取所有成员的词集列表
    @Override
    public List<Vocabulary> getVocListByClassesUser(String cid) {
        // 得到该班级的成员列表
        List<UserClasses> userClassesList = userClassesMapper.selectList(new QueryWrapper<UserClasses>().eq("cid", cid));
        ArrayList<Vocabulary> list = new ArrayList<>();
        for (UserClasses userClasses : userClassesList) {
            List<Vocabulary> vocabularyList = vocabularyService.getUserVocabularyListByUid(userClasses.getUid());
            list.addAll(vocabularyList);
        }
        return list;
    }

    // 根据用户ID获取班级 serviceImpl
    @Override
    public Classes getCLassesByUid(String uid) {
        UserClasses userClasses = userClassesMapper.selectOne(new QueryWrapper<UserClasses>().eq("uid", uid));
        if(userClasses == null) return null;
        Classes classes = classesMapper.selectOne(new QueryWrapper<Classes>().eq("id", userClasses.getCid()));
        return classes;
    }


}

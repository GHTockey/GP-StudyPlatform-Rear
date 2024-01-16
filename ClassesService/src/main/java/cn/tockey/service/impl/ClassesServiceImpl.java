package cn.tockey.service.impl;

import cn.tockey.domain.Classes;
import cn.tockey.domain.User;
import cn.tockey.domain.UserClasses;
import cn.tockey.feign.UserFeign;
import cn.tockey.mapper.ClassesMapper;
import cn.tockey.mapper.UserClassesMapper;
import cn.tockey.service.ClassesService;
import cn.tockey.vo.BaseResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
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

    @Override
    public Classes addClasses(Classes classes) {
        int i = classesMapper.insert(classes);
        if (i == 1) return classes;
        return null;
    }

    @Override
    public Classes getClasses(String id) {
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
}

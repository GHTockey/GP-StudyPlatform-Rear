package cn.tockey.controller;
import cn.tockey.domain.Classes;
import cn.tockey.domain.User;
import cn.tockey.domain.UserClasses;
import cn.tockey.domain.Vocabulary;
import cn.tockey.service.ClassesService;
import cn.tockey.service.UserClassesService;
import cn.tockey.config.JwtProperties;
import cn.tockey.utils.JwtUtils;
import cn.tockey.vo.BaseResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author tockey
 * @since 2024-01-15
 */
@RestController
@RequestMapping("/classes")
public class ClassesController {
    @Resource
    private ClassesService classesService;
    @Resource
    private UserClassesService userClassesService;
    @Resource
    private JwtProperties jwtProperties;
    @Resource
    private HttpServletRequest httpServletRequest;


    // jwt 解析用户信息
    public User parseUserByToken(String token) throws Exception {
        User user = JwtUtils.getObjectFromToken(token, jwtProperties.getPublicKey(), User.class);
        System.out.println("token解析；当前用户："+user);
        return user;
    }


    // 添加班级
    @PostMapping
    BaseResult<String> addClasses(@RequestBody Classes classes) throws Exception {
        User currentUser = parseUserByToken(httpServletRequest.getHeader("Authorization"));
        // 检查是否已加入班级
        List<UserClasses> userClasses = userClassesService.list(new QueryWrapper<UserClasses>().eq("uid", currentUser.getId()));
        if (!userClasses.isEmpty()) return BaseResult.error("你已加入班级，不可创建");

        Classes classesResult = classesService.addClasses(classes);
        if (classesResult != null) return BaseResult.ok("创建成功").append("id", classesResult.getId());
        return BaseResult.error("创建失败");
    }

    // 根据ID获取班级信息
    @GetMapping("/{id}")
    BaseResult<Classes> getClassesById(@PathVariable String id) {
        Classes classes = classesService.getClassesById(id);
        if (classes != null) return BaseResult.ok("获取成功", classes);
        return BaseResult.error("获取失败");
    }

    // 删除班级
    @DeleteMapping("/{id}")
    BaseResult<String> deleteClasses(@PathVariable String id) {
        Integer deleteResult = classesService.delClassesById(id);
        if (deleteResult != 0) return BaseResult.ok("删除成功");
        return BaseResult.error("删除失败");
    }

    // 修改班级
    @PutMapping
    BaseResult<String> updateClasses(@RequestBody Classes classes) {
        Integer updateResult = classesService.updClasses(classes);
        if (updateResult != 0) return BaseResult.ok("修改成功");
        return BaseResult.error("修改失败");
    }

    // 搜索班级
    @GetMapping("/search/{keyword}")
    BaseResult<List<Classes>> searchClasses(@PathVariable String keyword) {
        List<Classes> classesList = classesService.searchClassesList(keyword);
        return BaseResult.ok("搜索成功", classesList);
    }

    // 获取班级列表
    @GetMapping("/list")
    BaseResult<List<Classes>> getClassesList() {
        List<Classes> classesList = classesService.getClassesList();
        return BaseResult.ok("获取成功", classesList);
    }

    // 根据班级ID获取所有成员的词集列表
    @GetMapping("/user/voc/list/{cid}")
    BaseResult<List<Vocabulary>> getVocListByClassesUser(@PathVariable String cid){
        List<Vocabulary> list = classesService.getVocListByClassesUser(cid);
        return BaseResult.ok("获取成功",list);
    }

    // 班级移除用户 【关联表】
    @PostMapping("/user/remove")
    BaseResult<String> classesRemUser(@RequestBody UserClasses userClasses){
        Integer i = classesService.classesRemUser(userClasses);
        if (i>0) return BaseResult.ok("移除成功");
        return BaseResult.error("移除失败");
    }

    // 用户加入班级 【关联表】
    @PostMapping("/user/push")
    BaseResult<String> userAddClasses(@RequestBody UserClasses userClasses){
        // 限制一个用户只能加入一个班级
        UserClasses checkUserClasses = userClassesService.getOne(new QueryWrapper<UserClasses>().eq("uid", userClasses.getUid()));
        if (checkUserClasses != null) return BaseResult.error("您只能加入一个班级");
        Integer i = classesService.userAddClasses(userClasses);
        if (i>0) return BaseResult.ok("加入成功");
        return BaseResult.error("加入失败");
    }

    // 根据用户ID获取班级
    @GetMapping("/user/{uid}")
    BaseResult<Classes> getCLassesByUid(@PathVariable String uid){
        Classes classes = classesService.getCLassesByUid(uid);
        if (classes != null) return BaseResult.ok("获取成功",classes);
        return BaseResult.error("获取失败");
    }
}

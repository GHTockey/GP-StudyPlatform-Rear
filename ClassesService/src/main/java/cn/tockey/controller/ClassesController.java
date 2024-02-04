package cn.tockey.controller;

import cn.tockey.domain.Classes;
import cn.tockey.service.ClassesService;
import cn.tockey.vo.BaseResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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

    // 添加班级
    @PostMapping
    BaseResult<String> addClasses(@RequestBody Classes classes) {
        Classes classesResult = classesService.addClasses(classes);
        if (classesResult != null) return BaseResult.ok("添加成功").append("id", classesResult.getId());
        return BaseResult.error("添加失败");
    }

    // 获取班级
    @GetMapping("/{id}")
    BaseResult<Classes> getClasses(@PathVariable String id) {
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
        List<Classes> classesList = classesService.list(new QueryWrapper<Classes>().like("name", keyword));
        return BaseResult.ok("搜索成功", classesList);
    }

    // 获取班级列表
    @GetMapping("/list")
    BaseResult<List<Classes>> getClassesList() {
        List<Classes> classesList = classesService.getClassesList();
        return BaseResult.ok("获取成功", classesList);
    }
}

package cn.tockey.controller;

import cn.tockey.domain.Classes;
import cn.tockey.service.ClassesService;
import cn.tockey.vo.BaseResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
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
        Classes classes = classesService.getClasses(id);
        if (classes != null) return BaseResult.ok("获取成功", classes);
        return BaseResult.error("获取失败");
    }
}

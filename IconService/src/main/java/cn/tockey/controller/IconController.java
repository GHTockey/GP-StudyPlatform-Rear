package cn.tockey.controller;

import cn.tockey.domain.Icon;
import cn.tockey.service.IconService;
import cn.tockey.vo.BaseResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author tockey
 * @since 2024-02-01
 */
@RestController
@RequestMapping("/icon")
public class IconController {
    @Resource
    private IconService iconService;

    // 获取图标列表
    @GetMapping("/list")
    BaseResult<List<Icon>> getIconList(){
        List<Icon> iconList = iconService.list();
        if (iconList != null) return BaseResult.ok("获取成功", iconList);
        return BaseResult.error("获取失败");
    }

    // 添加图标
    @PostMapping
    BaseResult<String> addIcon(@RequestBody Icon icon){
        icon.setUpdateTime(null);
        boolean saved = iconService.save(icon);
        if(saved) return BaseResult.ok("添加成功");
        return BaseResult.error("添加失败");
    }

    // 删除图标
    @DeleteMapping("/{id}")
    BaseResult<String> deleteIcon(@PathVariable Integer id){
        boolean removed = iconService.removeById(id);
        if(removed) return BaseResult.ok("删除成功");
        return BaseResult.error("删除失败");
    }

    // 修改图标
    @PutMapping
    BaseResult<String> updateIcon(@RequestBody Icon icon){
        boolean updated = iconService.updateById(icon);
        if(updated) return BaseResult.ok("修改成功");
        return BaseResult.error("修改失败");
    }
}

package cn.tockey.controller;

import cn.tockey.domain.Icon;
import cn.tockey.domain.PermIcon;
import cn.tockey.service.IconService;
import cn.tockey.service.PermIconService;
import cn.tockey.vo.BaseResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

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
    @Resource
    private PermIconService permIconService;

    // 获取图标列表
    @Operation(summary = "获取图标列表")
    @GetMapping("/list")
    BaseResult<List<Icon>> getIconList(){
        //List<Icon> iconList = iconService.list();
        List<Icon> iconList = iconService.getIconList();
        if (iconList != null) return BaseResult.ok("获取成功", iconList);
        return BaseResult.error("获取失败");
    }

    // 添加图标
    @Operation(summary = "添加图标")
    @PostMapping
    BaseResult<String> addIcon(@RequestBody Icon icon){
        //icon.setUpdateTime(null);
        //boolean saved = iconService.save(icon);
        boolean saved = iconService.addIcon(icon);
        if(saved) return BaseResult.ok("添加成功");
        return BaseResult.error("添加失败");
    }

    // 删除图标
    @Operation(summary = "删除图标")
    @DeleteMapping("/{ids}")
    BaseResult<String> deleteIcon(@PathVariable Integer[] ids){
        // 删除前检查 权限关联
        for (Integer id : ids) {
            List<PermIcon> useIcons = permIconService.list(new QueryWrapper<PermIcon>().eq("icon_id", id));
            if(useIcons.size() > 0) return BaseResult.error("图标: " + id + " 已被" + useIcons.size() + "个权限使用, 无法删除");
        }

        //boolean removed = iconService.removeByIds(Arrays.asList(ids));
        boolean removed = iconService.delIcon(ids);
        if(removed) return BaseResult.ok("删除成功");
        return BaseResult.error("删除失败");
    }

    // 修改图标
    @Operation(summary = "修改图标")
    @PutMapping
    BaseResult<String> updateIcon(@RequestBody Icon icon){
        //boolean updated = iconService.updateById(icon);
        boolean updated = iconService.updIcon(icon);
        if(updated) return BaseResult.ok("修改成功");
        return BaseResult.error("修改失败");
    }

    // 获取图标 【根据id】
    @Operation(summary = "获取图标")
    @GetMapping("/{id}")
    BaseResult<Icon> getIconById(@PathVariable Integer id){
        //Icon icon = iconService.getById(id);
        Icon icon = iconService.getIconById(id);
        if (icon != null) return BaseResult.ok("获取成功", icon);
        return BaseResult.error("获取失败");
    }


    // 权限关联图标表
    // 权限移除图标
    @Operation
    @DeleteMapping("/perm/{permId}")
    BaseResult<String> removePermIcon(@PathVariable String permId){
        //boolean removed = permIconService.remove(new QueryWrapper<PermIcon>().eq("perm_id", permId));
        boolean removed = permIconService.removePermIcon(permId);
        if(removed) return BaseResult.ok("移除成功");
        return BaseResult.error("移除失败");
    }
    // 权限使用图标 【添加】
    @Operation(summary = "权限使用图标")
    @PostMapping("/perm")
    BaseResult<String> addPermIcon(@RequestBody PermIcon permIcon){
        PermIcon exitsPermIcon = permIconService.getOne(new QueryWrapper<PermIcon>().eq("perm_id", permIcon.getPermId()).eq("icon_id", permIcon.getIconId()));
        if (exitsPermIcon != null) return BaseResult.error("已存在相同的权限图标");

        //boolean saved = permIconService.save(permIcon);
        boolean saved = permIconService.addPermIcon(permIcon);
        if(saved) return BaseResult.ok("添加成功");
        return BaseResult.error("添加失败");
    }
    // 获取权限使用的图标 【根据权限id】
    @Operation(summary = "获取权限使用的图标")
    @GetMapping("/perm/{permId}")
    BaseResult<PermIcon> getPermIconIdByPermId(@PathVariable String permId){
        //PermIcon permIcon = permIconService.getOne(new QueryWrapper<PermIcon>().eq("perm_id", permId));
        PermIcon permIcon = permIconService.getPermIconIdByPid(permId);
        if (permIcon != null) return BaseResult.ok("获取成功", permIcon);
        return BaseResult.error("获取失败");
    }
}

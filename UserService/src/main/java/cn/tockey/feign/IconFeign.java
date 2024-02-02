package cn.tockey.feign;

import cn.tockey.domain.Icon;
import cn.tockey.domain.PermIcon;
import cn.tockey.vo.BaseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "icon-service",path = "/icon")
public interface IconFeign {
    // 权限移除图标
    @DeleteMapping("/perm/{permId}")
    BaseResult<String> removePermIcon(@PathVariable Integer permId);

    // 权限使用图标 【添加】
    @PostMapping("/perm")
    BaseResult<String> addPermIcon(@RequestBody PermIcon permIcon);

    // 获取图标 【根据id】
    @GetMapping("/{id}")
    BaseResult<Icon> getIconById(@PathVariable Integer id);

    // 获取权限使用的图标ID 【根据权限id】
    @GetMapping("/perm/{permId}")
    BaseResult<PermIcon> getPermIconIdByPermId(@PathVariable Integer permId);
}

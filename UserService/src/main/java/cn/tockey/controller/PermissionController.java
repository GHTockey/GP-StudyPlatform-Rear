package cn.tockey.controller;

import cn.tockey.domain.Permission;
import cn.tockey.service.PermissionService;
import cn.tockey.vo.BaseResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author tockey
 * @since 2024-01-16
 */
@RestController
@RequestMapping("/permission")
public class PermissionController {
    @Resource
    private PermissionService permissionService;

    // 根据角色id获取权限 controller
    @GetMapping("/user/{uid}")
    BaseResult<List<Permission>> getPermissionByUid(@PathVariable String uid) {
        List<Permission> permissionList = permissionService.getPermissionByUid(uid);
        if (permissionList != null) return BaseResult.ok("获取成功", permissionList);
        return BaseResult.error("获取失败");
    }
}

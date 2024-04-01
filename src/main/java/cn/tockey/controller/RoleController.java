package cn.tockey.controller;

import cn.tockey.domain.Role;
import cn.tockey.domain.RolePermission;
import cn.tockey.domain.User;
import cn.tockey.domain.UserRole;
import cn.tockey.service.RolePermissionService;
import cn.tockey.service.RoleService;
import cn.tockey.service.UserRoleService;
import cn.tockey.service.UserService;
import cn.tockey.vo.BaseResult;
import cn.tockey.vo.SetRolePermVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author tockey
 * @since 2024-01-16
 */
@RestController
@RequestMapping("/role")
public class RoleController {
    @Resource
    private RoleService roleService;
    @Resource
    private UserRoleService userRoleService;
    @Resource
    private UserService userService;
    @Resource
    private RolePermissionService rolePermissionService;

    // 获取角色列表
    @Operation(summary = "获取角色列表")
    @GetMapping("/list")
    BaseResult<List<Role>> getRoleList() {
        return BaseResult.ok("获取成功", roleService.getRoleList());
    }

    // 修改角色
    @Operation(summary = "修改角色")
    @PutMapping
    BaseResult<String> updateRole(@RequestBody Role role) {
        boolean updated = roleService.updateById(role);
        if (updated) return BaseResult.ok("修改成功");
        return BaseResult.error("修改失败");
    }

    // 添加角色
    @Operation(summary = "添加角色")
    @PostMapping
    BaseResult<String> addRole(@RequestBody Role role) {
        boolean saved = roleService.save(role);
        if (saved) return BaseResult.ok("添加成功");
        return BaseResult.error("添加失败");
    }

    // 删除角色
    @Operation(summary = "删除角色")
    @DeleteMapping("/{rid}")
    BaseResult<String> deleteRole(@PathVariable String rid) {
        // 检查关联用户
        List<UserRole> userRoleList = userRoleService.list(new QueryWrapper<UserRole>().eq("rid", rid));
        ArrayList<String> userNames = new ArrayList<>();
        for (UserRole userRole : userRoleList) {
            User user = userService.getOne(new QueryWrapper<User>().eq("id", userRole.getUid()));
            userNames.add(user.getUsername());
        }
        if (!userNames.isEmpty())
            return BaseResult.error("删除失败,该角色已被用户:" + userNames.stream().map(name -> "【" + name + "】").collect(Collectors.joining()) + "关联");

        // 检查关联权限
        List<RolePermission> rolePermList = rolePermissionService.list(new QueryWrapper<RolePermission>().eq("rid", rid));
        if (!rolePermList.isEmpty())
            return BaseResult.error("删除失败，该角色已关联" + rolePermList.size() + "项权限，请先删除关联的权限");

        boolean removed = roleService.removeById(rid);
        if (removed) return BaseResult.ok("删除成功");
        return BaseResult.error("删除失败");
    }

    // 设置角色权限
    @Operation(summary = "设置角色权限")
    @PostMapping("/perm/set/{rid}")
    BaseResult<String> setRolePermission(
            @PathVariable Integer rid,
            @RequestBody SetRolePermVo setRolePermVo
            ) {
        int i = roleService.addRole(rid, setRolePermVo);
        if (i > 0) return BaseResult.ok("设置成功");
        return BaseResult.error("设置失败");
    }
}

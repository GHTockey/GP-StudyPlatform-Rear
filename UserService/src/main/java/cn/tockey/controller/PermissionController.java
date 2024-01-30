package cn.tockey.controller;

import cn.tockey.domain.Permission;
import cn.tockey.domain.Role;
import cn.tockey.domain.RolePermission;
import cn.tockey.service.PermissionService;
import cn.tockey.service.RolePermissionService;
import cn.tockey.service.RoleService;
import cn.tockey.vo.BaseResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    @Resource
    private RolePermissionService rolePermissionService;
    @Resource
    private RoleService roleService;

    // 根据用户id获取权限 controller
    @GetMapping("/user/{uid}")
    BaseResult<List<Permission>> getPermissionByUid(@PathVariable String uid) {
        List<Permission> permissionList = permissionService.getPermissionByUid(uid);
        if (permissionList != null) return BaseResult.ok("获取成功", permissionList);
        return BaseResult.error("获取失败");
    }

    // 根据角色id获取权限 controller
    @GetMapping("/role/{rid}")
    BaseResult<List<Permission>> getPermissionByRid(@PathVariable Integer rid) {
        List<Permission> permissionList = permissionService.getPermissionByRid(rid);
        if (permissionList != null) return BaseResult.ok("获取成功", permissionList);
        return BaseResult.error("获取失败");
    }

    // 获取权限列表
    @GetMapping("/list")
    BaseResult<List<Permission>> getPermissionList(){
        List<Permission> permissionList = permissionService.list();
        if (permissionList != null) return BaseResult.ok("获取成功", permissionList);
        return BaseResult.error("获取失败");
    }

    // 添加权限
    @PostMapping
    BaseResult<String> addPermission(@RequestBody Permission permission){
        Permission exitsPermName = permissionService.getOne(new QueryWrapper<Permission>().eq("name", permission.getName()));
        if (exitsPermName != null) return BaseResult.error("已存在相同名称的权限");
        Permission exitsPermPath = permissionService.getOne(new QueryWrapper<Permission>().eq("path", permission.getPath()));
        if (exitsPermPath != null) return BaseResult.error("已存在相同路径的权限");
        int i = permissionService.addPermission(permission);
        if (i > 0) return BaseResult.ok("添加成功");
        return BaseResult.error("添加失败");
    }

    // 获取权限
    @GetMapping("/{id}")
    BaseResult<Permission> getPermission(@PathVariable String id){
        Permission permission = permissionService.getOne(new QueryWrapper<Permission>().eq("id", id));
        if (permission != null) return BaseResult.ok("获取成功", permission);
        return BaseResult.error("获取失败");
    }

    // 修改权限
    @PutMapping
    BaseResult<String> updPermission(@RequestBody Permission permission){
        if (permission.getId() == null) return BaseResult.error("id不能为空");
        boolean updated = permissionService.updateById(permission);
        if (updated) return BaseResult.ok("修改成功");
        return BaseResult.error("修改失败");
    }

    // 获取所有的父级id
    @GetMapping("/parents/{id}")
    BaseResult<List<Integer>> getAllPidById(@PathVariable Integer id){
        List<Integer> allPidById = permissionService.getAllPidById(id);
        return BaseResult.ok("获取成功", allPidById);
    }

    // 删除权限
    @DeleteMapping("/{id}")
    BaseResult<String> deletePermission(@PathVariable String id){
        // 检查是否有角色使用该权限
        List<RolePermission> rolePermList = rolePermissionService.list(new QueryWrapper<RolePermission>().eq("pid", id));
        ArrayList<String> roleNames = new ArrayList<>();
        for (RolePermission rolePermission : rolePermList) {
            Role role = roleService.getOne(new QueryWrapper<Role>().eq("id", rolePermission.getRid()));
            roleNames.add(role.getName());
        }
        if(!roleNames.isEmpty()) return BaseResult.error("该权限已被角色:" + roleNames.stream().map(name->"【"+name+"】").collect(Collectors.joining()) + "使用，无法删除");

        // 获取所有的子级id
        List<Integer> allChildIdById = permissionService.getAllChildIdById(Integer.parseInt(id));
        // 删除所有的子级id
        boolean removed = permissionService.removeByIds(allChildIdById);

        // 删除自己
        boolean deleted = permissionService.removeById(id);
        if (deleted) return BaseResult.ok("删除成功");
        return BaseResult.error("删除失败");
    }
}

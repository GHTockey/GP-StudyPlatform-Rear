package cn.tockey.controller;

import cn.tockey.domain.Role;
import cn.tockey.domain.User;
import cn.tockey.domain.UserRole;
import cn.tockey.service.RoleService;
import cn.tockey.service.UserRoleService;
import cn.tockey.service.UserService;
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
@RequestMapping("/role")
public class RoleController {
    @Resource
    private RoleService roleService;
    @Resource
    private UserRoleService userRoleService;
    @Resource
    private UserService userService;

    // 获取角色列表
    @GetMapping("/list")
    BaseResult<List<Role>> getRoleList() {
        return BaseResult.ok("获取成功", roleService.getRoleList());
    }

    // 修改角色
    @PutMapping
    BaseResult<String> updateRole(@RequestBody Role role) {
        boolean updated = roleService.updateById(role);
        if (updated) return BaseResult.ok("修改成功");
        return BaseResult.error("修改失败");
    }

    // 添加角色
    @PostMapping
    BaseResult<String> addRole(@RequestBody Role role) {
        boolean saved = roleService.save(role);
        if (saved) return BaseResult.ok("添加成功");
        return BaseResult.error("添加失败");
    }

    // 删除角色
    @DeleteMapping("/{id}")
    BaseResult<String> deleteRole(@PathVariable String id){
        // 检查关联
        List<UserRole> userRoleList = userRoleService.list(new QueryWrapper<UserRole>().eq("rid", id));
        ArrayList<String> userNames = new ArrayList<>();
        for (UserRole userRole : userRoleList) {
            User user = userService.getOne(new QueryWrapper<User>().eq("id", userRole.getUid()));
            userNames.add(user.getUsername());
        }
        if (userNames.size() > 0) return BaseResult.error("删除失败,该角色已被用户:" + userNames.stream().map(name->"【"+name+"】").collect(Collectors.joining()) + "关联");

        boolean removed = roleService.removeById(id);
        if (removed) return BaseResult.ok("删除成功");
        return BaseResult.error("删除失败");
    }
}

package cn.tockey.controller;

import cn.tockey.domain.Role;
import cn.tockey.service.RoleService;
import cn.tockey.vo.BaseResult;
import org.springframework.web.bind.annotation.GetMapping;
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
@RequestMapping("/role")
public class RoleController {
    @Resource
    private RoleService roleService;

    // 获取角色列表
    @GetMapping("/list")
    BaseResult<List<Role>> getRoleList() {
        return BaseResult.ok("获取成功", roleService.list());
    }
}

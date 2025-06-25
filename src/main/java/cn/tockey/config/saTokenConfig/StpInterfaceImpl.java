package cn.tockey.config.saTokenConfig;

import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpUtil;
import cn.tockey.domain.Permission;
import cn.tockey.domain.Role;
import cn.tockey.service.PermissionService;
import cn.tockey.service.RoleService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义权限验证接口扩展
 */
@Component
public class StpInterfaceImpl implements StpInterface {
    @Resource
    private PermissionService permissionService;
    @Resource
    private RoleService roleService;

    /**
     * 返回当前登录账号所拥有的权限集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        //// 本 list 仅做模拟，实际项目中要根据具体业务逻辑来查询权限
        //List<String> list = new ArrayList<String>();
        //list.add("101");
        //list.add("user.add");
        //list.add("user.update");
        //list.add("user.get");
        //// list.add("user.delete");
        //list.add("art.*");
        //return list;

        // 从数据库中获取权限列表
        ArrayList<String> list = new ArrayList<>();
        List<Permission> permissionList = permissionService.getAllPermissionListByUid(StpUtil.getLoginId().toString());
        for (Permission permission : permissionList) {
            //System.out.println(permission.getPath());
            list.add(permission.getPath());
        }
        return list;
    }

    /**
     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        //// 本 list 仅做模拟，实际项目中要根据具体业务逻辑来查询角色
        //List<String> list = new ArrayList<String>();
        //list.add("admin");
        //list.add("super-admin");
        //return list;

        // 从数据库中获取角色列表
        ArrayList<String> list = new ArrayList<>();
        List<Role> roleList = roleService.getRoleListByUid(StpUtil.getLoginId().toString());
        for (Role role : roleList) {
            list.add(role.getName());
        }
        return list;
    }
}

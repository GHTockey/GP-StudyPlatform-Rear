package cn.tockey.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.List;

@Data
@TableName("users")
public class User {
    @TableId(value = "id", type = IdType.AUTO)
    private String id;
    private String username;
    private String password;
    private String avatar;
    private String sex;
    private String email;

    // 关联
    @TableField(exist = false)
    private List<Permission> permissionList; // 权限列表
    @TableField(exist = false)
    private List<Role> roleList; // 角色列表
    @TableField(exist = false)
    private Classes classes; // 班级
}
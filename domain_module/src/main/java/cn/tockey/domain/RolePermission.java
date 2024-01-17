package cn.tockey.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author tockey
 * @since 2024-01-16
 */
@Data
@TableName("role_permission")
public class RolePermission {

    /**
     * 角色ID
     */
    private Integer rid;

    /**
     * 权限ID
     */
    private Integer pid;
}

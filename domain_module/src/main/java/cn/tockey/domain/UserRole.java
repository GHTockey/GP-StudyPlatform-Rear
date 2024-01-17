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
@TableName("user_role")
public class UserRole {

    /**
     * 用户ID
     */
    private String uid;

    /**
     * 角色ID
     */
    private Integer rid;
}

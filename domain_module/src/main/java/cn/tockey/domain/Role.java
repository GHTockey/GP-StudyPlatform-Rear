package cn.tockey.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
@TableName("role")
public class Role {

    /**
     * 角色ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 角色名称
     */
    private String name;
    // 角色描述
    @TableField("`desc`")
    private String desc;
}

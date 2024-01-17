package cn.tockey.domain;

import com.baomidou.mybatisplus.annotation.IdType;
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
@TableName("permission")
public class Permission {

    /**
     * 权限ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 权限名称
     */
    private String name;

    /**
     * 父级权限ID
     */
    private Integer parentId;

    /**
     * 权限地址
     */
    private String path;
}

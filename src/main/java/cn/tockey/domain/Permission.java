package cn.tockey.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.List;

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

    // 类型
    private String type;


    // 关联
    @TableField(exist = false)
    private List<Permission> children; // 树形结构的子节点
    @TableField(exist = false)
    private Integer halfCheck; // 树形结构的半选状态
    @TableField(exist = false)
    private Icon icon; // 图标
}

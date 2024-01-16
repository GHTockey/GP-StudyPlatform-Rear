package cn.tockey.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author tockey
 * @since 2024-01-15
 */
@Data
public class Classes {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 创建者ID
     */
    private String creatorUid;

    /**
     * 班级名称
     */
    private String name;

    /**
     * 描述
     */
    private String info;

    /**
     * 公告
     */
    private String annc;


    // 关联
    @TableField(exist = false)
    private List<User> userList;
}

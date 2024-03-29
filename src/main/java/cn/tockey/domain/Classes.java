package cn.tockey.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.ArrayList;
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

    // 创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String createTime;


    // 关联
    @TableField(exist = false)
    private List<User> userList = new ArrayList<>(); // 班级成员
    @TableField(exist = false)
    private User creator; // 创建者
}

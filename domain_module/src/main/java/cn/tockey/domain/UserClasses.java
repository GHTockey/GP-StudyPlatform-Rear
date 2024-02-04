package cn.tockey.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author tockey
 * @since 2024-01-15
 */
@Data
@AllArgsConstructor
@TableName("user_classes")
public class UserClasses {

    /**
     * 班级ID
     */
    @TableId("cid")
    private Integer cid;

    /**
     * 用户ID
     */
    private String uid;
}

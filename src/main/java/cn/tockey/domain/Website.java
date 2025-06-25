package cn.tockey.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * <p>
 * 
 * </p>
 *
 * @author tockey
 * @since 2024-04-07
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("website")
public class Website {
    @TableId(value = "id")
    private Integer id;
    // 网站 logo
    @TableField("logo_url")
    private String logoUrl;
    // 网站名称
    private String name;
    // 网站信息
    private String info;
}

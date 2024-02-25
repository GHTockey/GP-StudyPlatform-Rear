package cn.tockey.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("icon")
public class Icon {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private String name;
    private String code;
    private String creator;
    private String createTime;
    private String updateTime;
}

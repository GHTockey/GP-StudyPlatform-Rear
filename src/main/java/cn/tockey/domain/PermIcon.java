package cn.tockey.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@TableName("perm_icon")
public class PermIcon {
    private Integer permId;
    private Integer iconId;
}

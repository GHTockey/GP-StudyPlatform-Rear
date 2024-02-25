package cn.tockey.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

// 词条表
@Data
@TableName("words")
@AllArgsConstructor
public class Words {
    @TableId(value = "id", type = IdType.AUTO)
    private String id;
    private String vId; // 所属词集id
    private String word; // 词语
    private String definition; // 定义
}

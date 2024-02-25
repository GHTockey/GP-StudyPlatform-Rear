package cn.tockey.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

@TableName("user_vocabulary")
@Data
@AllArgsConstructor
public class UserVocabulary {
    private String uid;
    private String vid;
}

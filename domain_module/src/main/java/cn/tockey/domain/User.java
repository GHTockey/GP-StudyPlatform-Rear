package cn.tockey.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@TableName("users")
public class User {
    @Id
    @TableId(value = "id", type = IdType.AUTO)
    private String id;
    private String username;
    private String password;
    private String avatar;
    private String email;
}
